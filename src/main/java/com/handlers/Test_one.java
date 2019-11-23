package com.handlers;

import com.annotations.Requsetmapping;

@Requsetmapping({"/bbb","/ccc"})
public class Test_one {

    public String ccc(){
        return "Success";
    }

}
