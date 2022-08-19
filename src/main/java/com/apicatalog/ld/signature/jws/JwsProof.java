package com.apicatalog.ld.signature.jws;

import com.apicatalog.ld.signature.proof.Proof;
import com.apicatalog.ld.signature.proof.VerificationMethod;

import java.net.URI;
import java.time.Instant;

/**
 * Represents VC/VP proof.
 *
 * @see <a href="https://w3c-ccg.github.io/data-integrity-spec/#proofs">Proofs</a>
 *
 * Based on {@link Proof}
 *
 * @author petr apeltauer, KAPRION Technologies GmbH
 */
public class JwsProof {


    protected String type;
    protected URI purpose;
    protected VerificationMethod verificationMethod;
    protected Instant created;
    protected String domain;
    protected String jws;

    /**
     * The proof type used.
     *
     * For example, an Ed25519Signature2020 type indicates that the proof includes
     * a digital signature produced by an ed25519 cryptographic key.
     *
     * @return the proof type
     */
    public String getType() {
        return type;
    }

    /**
     * The intent for the proof, the reason why an entity created it.
     * e.g. assertion or authentication
     *
     * @see <a href="https://w3c-ccg.github.io/data-integrity-spec/#proof-purposes">Proof Purposes</a>
     *
     * @return {@link URI} identifying the purpose
     */
    public URI getPurpose() {
        return purpose;
    }

    /**
     * A set of parameters required to independently verify the proof,
     * such as an identifier for a public/private key pair that would be used in the proof.
     *
     * @return {@link VerificationMethod} to verify the proof signature
     */
    public VerificationMethod getVerificationMethod() {
        return verificationMethod;
    }

    /**
     * The string value of an ISO8601.
     *
     * @return the date time when the proof has been created
     */
    public Instant getCreated() {
        return created;
    }

    /**
     * A string value specifying the restricted domain of the proof.
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @return Json Web Signature
     */
    public String getJws() {
        return jws;
    }

}