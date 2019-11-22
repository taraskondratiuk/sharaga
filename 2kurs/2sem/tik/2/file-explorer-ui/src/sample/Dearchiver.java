package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;



public class Dearchiver {
    public static void decodeFile (File file) throws IOException, CloneNotSupportedException {
        byte arrCoded[] = Files.readAllBytes(file.toPath());
        StringBuilder decodedfile = getCodedLine(arrCoded);


        Node lastRoot = inOrderDecode(decodedfile);


        StringBuilder fin = new StringBuilder(decode(decodedfile.substring(lastRoot.frequency), lastRoot));
        File filenew = new File("D://1PROGA",file.getName().substring(0,file.getName().indexOf('.'))+"(unarchived)"+file.getName().substring(file.getName().indexOf('.'),file.getName().lastIndexOf('.')));
        FileOutputStream fil = new FileOutputStream(filenew);
        byte arr[] = new byte[fin.length()/8];
        int j = 0;
        for (int i = 0; i < fin.length(); i+=8) {
            StringBuilder buff = new StringBuilder(fin.substring(i, i+8));
            if (buff.charAt(0) == '1') {
                if (buff.toString().equals("10000000"))
                    arr[j] = -128;
                else {
                    buff.setCharAt(0, '0');
                    arr[j] = (byte) (0 - Byte.parseByte(buff.toString(), 2));
                }
            }
            else {
                arr[j] =  Byte.parseByte(buff.toString(), 2);
            }
            j++;
        }

        fil.write(arr);
        filenew.createNewFile();

    }

    public static Node inOrderDecode(StringBuilder code) {
        //System.out.println("full code "+ code);

        /*String len = code.substring(0, 16);///////////////////////
        int intLen = Integer.parseInt(len, 2);
        //System.out.println(intLen);
        code =new StringBuilder(code.substring(16));*///////////////////////////
        //System.out.println("full code "+ code);
        Node root = new Node(-1, null);

        int calc = 0;
        for (int i = 0; ;i++) {
            char x = code.charAt(i);
            //System.out.println("i :  "+i+" x :"+x);
            if (root.frequency == -1) {
                calc++;
                if (calc == 3) {
                    root.frequency = i;
                    return root;
                }
            }

            switch (x) {

                case ('0') :
                    if (root.leftChild == null) {
                        root.leftChild = new Node(0, null);
                        root.leftChild.prev = root;
                        root = root.leftChild;



                    }
                    else if (root.rightChild == null) {
                        root.rightChild = new Node(0, null);
                        root.rightChild.prev = root;
                        root = root.rightChild;

                    }
                    else {

                            root = root.prev;

                    }
                    break;
                case ('1') :
                    if (root.leftChild == null) {
                        StringBuilder builder = new StringBuilder(code.substring(i + 1, i + 9));

                        byte val;
                        if (builder.charAt(0) == '1') {
                            if (builder.toString().equals("10000000"))
                                val = -128;
                            else {
                                builder.setCharAt(0, '0');
                                val = (byte)(0 - Byte.parseByte(builder.toString(), 2));
                            }
                        } else {
                            val = Byte.parseByte(builder.toString(), 2);
                        }

                        root.leftChild = new Node(0, val);
                        i+=8;

                    }
                    else if (root.rightChild == null) {
                        StringBuilder builder = new StringBuilder(code.substring(i + 1, i + 9));
                        byte val;

                        if (builder.charAt(0) == '1') {
                            if (builder.toString().equals("10000000"))
                                val = -128;
                            else {
                                builder.setCharAt(0, '0');
                                val = (byte)(0 - Byte.parseByte(builder.toString(), 2));
                            }
                        } else {
                            val = Byte.parseByte(builder.toString(),2);
                        }

                        root.rightChild = new Node(0, val);

                        i+=8;







                    }

                    break;
            }
        }
        /*while (intLen %8 !=0) {
            intLen++;
        }
        root.frequency = intLen+16;
        return root;*/

    }

    public static StringBuilder getCodedLine(byte arr[]) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte by: arr) {
            if (by >= 0) {
                StringBuilder str = new StringBuilder(Integer.toBinaryString(by));
                str.reverse();
                while (str.length() != 8)
                    str.append("0");
                str.reverse();
                stringBuilder.append(str);
            }
            else{
                if (by == -128)
                    stringBuilder.append("10000000");
                else {
                    by = (byte) (0 - by);
                    StringBuilder str = new StringBuilder(Integer.toBinaryString(by));
                    str.reverse();

                    while (str.length() != 7)
                        str.append("0");
                    str.append("1");
                    str.reverse();
                    stringBuilder.append(str);
                }
            }
        }

        return stringBuilder;
    }


    public static Node check(Node rootClone, Node root) throws CloneNotSupportedException {
        if (rootClone == null) {
            rootClone = root.clone();
        }
        return rootClone;
    }
    public static String decode(String bits, Node root) throws CloneNotSupportedException {
        //System.out.println(bits);
        StringBuilder fil = new StringBuilder(bits);
        StringBuilder fil1 =new StringBuilder(fil.substring(fil.length() - 32)) ;

        int size = Integer.parseInt(fil1.toString(), 2);

        bits = bits.substring(0,size);

        bits += "0";
        StringBuilder result = new StringBuilder();
        Node rootClone = root.clone();

        for (int i = 0; i < bits.length(); i++) {
            rootClone = check(rootClone, root);

            switch (bits.charAt(i)) {
                case ('1'):
                    if (rootClone.rightChild != null) {
                        rootClone = rootClone.rightChild;
                    } else {
                        result.append(Node.getBinary(rootClone));
                        rootClone = null;
                        i--;
                    }
                    break;

                case ('0'):
                    if (rootClone.leftChild != null) {
                        rootClone = rootClone.leftChild;
                    } else {
                        result.append(Node.getBinary(rootClone));
                        rootClone = null;
                        i--;
                    }
                    break;

            }




        }
        return result.toString();
    }


}
