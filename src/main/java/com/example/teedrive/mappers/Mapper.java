package com.example.teedrive.mappers;

public interface Mapper <A, B>{
    B mapTo (A a);

    A mapFrom (B b);
}
