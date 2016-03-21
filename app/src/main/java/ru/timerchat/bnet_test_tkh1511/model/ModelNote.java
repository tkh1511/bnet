package ru.timerchat.bnet_test_tkh1511.model;

/**
 * Created by Timur on 22.03.16.
 */
public class ModelNote {



    private String id, body;
    private long da, dm;


    public ModelNote() {

    }


    public ModelNote(String id, String body, long da, long dm) {

        this.id = id;
        this.body = body;
        this.da = da;
        this.dm = dm;
    }




    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDa() {
        return da;
    }

    public void setDa(long da) {
        this.da = da;
    }

    public long getDm() {
        return dm;
    }

    public void setDm(long dm) {
        this.dm = dm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }








}

