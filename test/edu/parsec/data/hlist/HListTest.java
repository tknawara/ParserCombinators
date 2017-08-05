package edu.parsec.data.hlist;

public class HListTest {
    public static void main(String[] args) {
        Object a = new HCons<>(1, HNil.nil());
        System.out.println(a);
    }
}
