package com.chaos.object.id.util;

import com.chaos.object.id.core.ObjectId;
import org.hashids.Hashids;

/**
 * Created by zcfrank1st on 21/10/2016.
 */
public class HashIds {
    private ObjectId objectId;

    public HashIds(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String value() {
        Hashids hashids = new Hashids("chaos_salt");
        return hashids.encode(objectId.getObjectId());
    }
}
