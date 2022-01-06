package com.traffappscorelib.wsc;

import java.io.Serializable;

public class DbCountItem implements Serializable {
    public Integer count;

    public DbCountItem(Integer count) {
        this.count = count;
    }
}
