package com.apicatalog.ld.signature.proof;

import java.net.URI;
import java.time.Instant;

/**
 *
 *
 * see {@link https://w3c-ccg.github.io/data-integrity-spec/#proofs}
 *
 */
public interface Proof {

    /**
     * The proof type used.
     *
     * For example, an Ed25519Signature2020 type indicates that the proof includes
     * a digital signature produced by an ed25519 cryptographic key.
     *
     * @return
     */
    String getType();

    /**
     * The intent for the proof, the reason why an entity created it. 
     * e.g. assertion or authentication
     * 
     * see {@link https://w3c-ccg.github.io/data-integrity-spec/#proof-purposes}
     *
     * @return
     */
    URI getPurpose();

    /**
     * A set of parameters required to independently verify the proof,
     * such as an identifier for a public/private key pair that would be used in the proof.
     *
     * @return
     */
    VerificationMethod getVerificationMethod();

    /**
     * The string value of an ISO8601.
     *
     * @return
     */
    Instant getCreated();

    /**
     * A string value specifying the restricted domain of the proof.
     * @return
     */
    String getDomain();

    /**
     * One of any number of valid representations of proof value generated by the Proof Algorithm.
     * @return
     */
    byte[] getValue();
}
