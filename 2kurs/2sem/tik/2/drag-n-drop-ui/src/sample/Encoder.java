package sample;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Encoder {
    public static StringBuilder finTree (StringBuilder tree) {
        String len = Integer.toBinaryString((tree.length()));
        StringBuilder finLen = new StringBuilder();
        for (int i = 0; i < 16-len.length(); i++) {
            finLen.append("0");
        }
        finLen.append(len);
        //str.append(Integer.toBinaryString(str.length()));
        while (tree.length() % 8 != 0) {
            tree.append("0");
        }
        finLen.append(tree);
        //System.out.println("stroka:"+finLen);


        return finLen;
    }

    public static void encodeFile(File file) throws IOException, CloneNotSupportedException {

        byte arr[] = Files.readAllBytes(file.toPath());


        HashMap<Byte, Integer> charFrequencies = new HashMap<>();


        //fil.write(arr1);
        //filenew.createNewFile();
        // для простоты допустим, что количество символов не превосходит 256

        // считываем символы и считаем их частоту

        for(byte b : arr) {
            if (charFrequencies.containsKey(b))
                charFrequencies.put(b, charFrequencies.get(b)+1);
            else
                charFrequencies.put(b,1);
        }


        HuffmanTree tree = HuffmanTree.buildHuffmanTree(charFrequencies);

        System.out.printf("size before compression = %d%n", arr.length * 8);
        String incoded = tree.incode(arr);
        tree.printCodes();
        //System.out.println("incoded result = " + incoded);
        System.out.printf("data size after compression = %d%n", incoded.length());
        StringBuilder str = new StringBuilder();
        //root.Inorder(root);
        //System.out.println("gshdfoj:"+root.leftChild.leftChild.character);
        str = tree.inOrderCode(str,tree.root);
        str = Encoder.finTree(str);

        //str.deleteCharAt(str.length()-1);

        System.out.println("tree incoded : " + str);
/*

        //*Node newroot=inOrderDecode(str);
        StringBuilder str1 = new StringBuilder();
        inOrderCode(str1,newroot);
        System.out.println("tree incoded : " + str1);
        if(str.toString().equals(str1.toString()))
            System.out.println("ebat' ty krasavchik");//*/


        //StringBuilder str1 = tree.getFinalTreeCode(String.valueOf(str));
        StringBuilder str2 = tree.getFinalFileCode(str.length(), incoded);
        //System.out.println(str2);
        //System.out.println(str1.length()+str2.length());
        str.append(str2);
        System.out.println("coded file : "+str2);


        //System.out.println("strlen "+str1.length());
        byte arr1[] = tree.getByteCode(str);
        System.out.println("total size after compression : "+arr1.length*8);

        File filenew = new File("C://Users/тарас/desktop",file.getName().substring(0, file.getName().indexOf('.') + 1) + "huf");

        filenew.setWritable(true);////////////////////////////
        filenew.setReadable(false);
        FileOutputStream fil = new FileOutputStream(filenew);
        fil.write(arr1);
        filenew.createNewFile();//*/////////////////////////////////*/
    }
}
