/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.vector;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author ronaldo
 */
public class TokenVector extends Vector {

    int count = 0;
    String delim = null;
    Map<String, Integer> map = new HashMap();
    Map<Integer, String> mapp = new HashMap();

    public TokenVector(String str, String delim) {

        this.delim = delim;

        StringTokenizer tokenizer = new StringTokenizer(str, delim);

        while (tokenizer.hasMoreElements()) {
            map.compute(tokenizer.nextToken(), (t, u) -> {

                this.count++;

                if (map.get(t) != null) {
                    return u + 1;
                }

                return 1;

            });
        }

        int i = 0;
        for (String k : map.keySet()) {
            mapp.put(i++, k);
        }

        for (i = 0; i < map.size(); i++) {
            this.vector.add((double) map.get(mapp.get(i)) / (double) this.count);
        }

    }

    public TokenVector(TokenVector tokenMap, String str) {

        this.delim = tokenMap.delim;

        StringTokenizer tokenizer = new StringTokenizer(str, tokenMap.delim);

        for (String string : tokenMap.map.keySet()) {
            this.map.put(string, 0);
        }

        while (tokenizer.hasMoreElements()) {
            map.computeIfPresent(tokenizer.nextToken(), (t, u) -> {

                this.count++;

                if (map.get(t) != null) {
                    return u + 1;
                }

                return 1;

            });
        }

        for (Integer k : tokenMap.mapp.keySet()) {
            mapp.put(k, tokenMap.mapp.get(k));
        }

        for (int i = 0; i < map.size(); i++) {
            this.vector.add((double) map.get(mapp.get(i)) / (double) this.count);
        }

    }

    @Override
    public String toString() {

        String v = "[";

        for (double d : this.vector) {
            v += String.valueOf(d) + ",";
        }

        v += "]";

        return "TokenMap{" + "vector=" + v + ", count=" + count + ", delim=" + delim + ", map=" + map + ", mapp=" + mapp + '}';
    }

}
