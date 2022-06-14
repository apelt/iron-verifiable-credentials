package com.apicatalog.ld.signature.ed25519;

import com.apicatalog.ld.signature.SignatureSuite;
import com.apicatalog.ld.signature.primitive.MessageDigest;
import com.apicatalog.ld.signature.primitive.Urdna2015;

public class Ed25519Signature2020 extends SignatureSuite {

    public static final String TYPE = "https://w3id.org/security#Ed25519Signature2020";

    public Ed25519Signature2020() {
        super(
            TYPE,
            new Urdna2015(),
            new MessageDigest("SHA-256"),
            new SunSignatureProvider("Ed25519")
            );
    }
}