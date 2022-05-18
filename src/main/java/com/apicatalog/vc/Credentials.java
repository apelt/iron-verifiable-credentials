package com.apicatalog.vc;

import java.io.InputStream;
import java.io.Reader;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;

public interface Credentials extends StructuredData {

    static Credentials from(JsonStructure json) {
        
        if (json == null) {
            throw new IllegalArgumentException("Parameter 'json' must not be null.");
        }

        final JsonDocument document = JsonDocument.of(json);
        
        try {
            final JsonArray expanded = JsonLd.expand(document).get();

        } catch (JsonLdError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
//        final boolean verifiable = expanded.containsKey(Keywords.PROOF);
        
        final Credentials credentials = new ImmutableVerifiableCredentials(null, null); 
                
        return credentials;
    }
    
    static Credentials from(Reader reader) {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            return from(jsonReader.read());
        }
    }

    static Credentials from(InputStream is) {
        try (JsonReader jsonReader = Json.createReader(is)) {
            return from(jsonReader.read());
        }
    }
}
