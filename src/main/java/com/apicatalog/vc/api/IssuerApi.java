package com.apicatalog.vc.api;

import java.net.URI;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.JsonUtils;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.SchemeRouter;
import com.apicatalog.ld.signature.DataError;
import com.apicatalog.ld.signature.LinkedDataSignature;
import com.apicatalog.ld.signature.SigningError;
import com.apicatalog.ld.signature.SigningError.Code;
import com.apicatalog.ld.signature.ed25519.Ed25519KeyPair2020;
import com.apicatalog.ld.signature.ed25519.Ed25519Proof2020;
import com.apicatalog.ld.signature.ed25519.Ed25519Signature2020;
import com.apicatalog.ld.signature.ed25519.Ed25519VerificationKey2020;
import com.apicatalog.ld.signature.key.KeyPair;
import com.apicatalog.ld.signature.proof.EmbeddedProof;
import com.apicatalog.ld.signature.proof.ProofOptions;
import com.apicatalog.vc.StaticContextLoader;
import com.apicatalog.vc.Verifiable;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public final class IssuerApi extends CommonApi<IssuerApi> {

    private final URI location;
    private final JsonObject document;

    private final URI keyPairLocation;
    private final KeyPair keyPair;

    private final ProofOptions options;

    protected IssuerApi(URI location, URI keyPairLocation, ProofOptions options) {
        this.location = location;
        this.document = null;

        this.keyPairLocation = keyPairLocation;
        this.keyPair = null;

        this.options = options;
    }

    protected IssuerApi(JsonObject document, KeyPair keyPair, ProofOptions options) {
        this.document = document;
        this.location = null;

        this.keyPair = keyPair;
        this.keyPairLocation = null;

        this.options = options;
    }

    /**
     * Get signed document in expanded form.
     *
     * @return
     * @throws SigningError
     * @throws DataError
     */
    public JsonObject getExpanded() throws SigningError, DataError {

        if (loader == null) {
            // default loader
            loader = SchemeRouter.defaultInstance();
        }

        if (bundledContexts) {
            loader = new StaticContextLoader(loader);
        }

        if (document != null && keyPair != null)  {
            return sign(document, keyPair, options);
        }

        if (location != null && keyPairLocation != null)  {
            return sign(location, keyPairLocation, options);
        }

        throw new IllegalStateException();
    }

    /**
     * Get signed document in compacted form.
     *
     * @param context
     * @return
     * @throws SigningError
     * @throws DataError
     */
    public JsonObject getCompacted(final URI context)  throws SigningError, DataError {

        final JsonObject signed = getExpanded();

        try {
            return JsonLd.compact(JsonDocument.of(signed), context).loader(loader).get();
        } catch (JsonLdError e) {
            throw new SigningError(e);
        }
    }

    private final JsonObject sign(final URI documentLocation, final URI keyPairLocation, final ProofOptions options) throws DataError, SigningError {
        try {
            // load the document
            final JsonArray expanded = JsonLd.expand(documentLocation).loader(loader).base(base).get();

            // load key pair
            final JsonArray keys = JsonLd.expand(keyPairLocation).loader(loader).get();

            for (final JsonValue key : keys) {
 
                // take the first key that match
                if (!Ed25519VerificationKey2020.isIstanceOf(key)) {

                    final Ed25519KeyPair2020 keyPair = Ed25519KeyPair2020.from(key.asJsonObject()); 
                    
                    return sign(expanded, keyPair, options);
                }                
            }

            throw new SigningError(Code.UnknownVerificatioonKey);

        } catch (JsonLdError e) {
            throw new SigningError(e);
        }
    }

    private final JsonObject sign(JsonObject document, KeyPair keyPair, ProofOptions options) throws DataError, SigningError {
        try {
            // load the document
            final JsonArray expanded = JsonLd.expand(JsonDocument.of(document)).loader(loader).base(base).get();

            return sign(expanded, keyPair, options);

        } catch (JsonLdError e) {
            throw new SigningError(e);
        }
    }

    private static final JsonObject sign(final JsonArray expanded, final KeyPair keyPair, final ProofOptions options) throws SigningError, DataError {

        final JsonObject object = JsonUtils.findFirstObject(expanded).orElseThrow(() ->
                    new SigningError() // malformed input, not single object to sign has been found
                    //TODO ErrorCode
                );

        final Verifiable verifiable = Vc.get(object);

        // is expired?
        if (verifiable.isCredential() && verifiable.asCredential().isExpired()) {
            throw new SigningError(Code.Expired);
        }

        final JsonObject data = EmbeddedProof.removeProof(object);
        final EmbeddedProof proof = Ed25519Proof2020.from(options);

        final LinkedDataSignature suite = new LinkedDataSignature(new Ed25519Signature2020());

        byte[] signature = suite.sign(data, keyPair, proof.toJson());

        proof.setValue(signature);

        return proof.addProofTo(object);
    }
}
