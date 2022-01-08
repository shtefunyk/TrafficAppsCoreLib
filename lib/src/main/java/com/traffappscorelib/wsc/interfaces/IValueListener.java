package com.traffappscorelib.wsc.interfaces;

public interface IValueListener<T> {
    void value(T result);
    void failed();
}
