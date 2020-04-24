package com.sayo.qa;

class Demo {
    public void add(Byte b)
    {
        b = b++;
    }
    public void test()
    {
        Byte a = 127;
        Byte b = 127;
        add(++a);
        System.out.print(a + " ");
        add(b);
        System.out.print(b + "");
    }

    public static void main(String[] args) {
        Demo d = new Demo();
       d.test();
    }

}