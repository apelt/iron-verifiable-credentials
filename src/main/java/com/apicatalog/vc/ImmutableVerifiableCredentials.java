package com.apicatalog.vc;

import com.apicatalog.lds.LinkedDataSignature;
import com.apicatalog.lds.ed25519.Ed25519Signature2020;
import com.apicatalog.lds.proof.Proof;

import jakarta.json.JsonArray;

class ImmutableVerifiableCredentials implements VerifiableCredentials {

    final Proof proof;
    final JsonArray document;

    public ImmutableVerifiableCredentials(Credentials credentials, Proof proof, JsonArray document) {
        this.proof = proof;
        this.document = document;
    }

    @Override
    public Proof getProof() {
        return proof;
    }

    public JsonArray getExpandedDocument() {
        return document;
    }

}
