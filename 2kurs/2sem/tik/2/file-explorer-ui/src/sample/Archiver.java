package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class Archiver {
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


        for(byte b : arr) {
            if (charFrequencies.containsKey(b))
                charFrequencies.put(b, charFrequencies.get(b)+1);
            else
                charFrequencies.put(b,1);
        }

        Tree tree = Tree.buildHuffmanTree(charFrequencies);

        System.out.printf("size before compression = %d%n", arr.length * 8);
        String incoded = tree.incode(arr);
        tree.printCodes();

        System.out.printf("data size after compression = %d%n", incoded.length());

        StringBuilder str = tree.inOrderCode(tree.root);

        System.out.println("tree encoded : " + str);

        StringBuilder str2 = tree.getFinalFileCode(str.length(), incoded);

        str.append(str2);
        System.out.println("file encoded : "+str2);

        byte arr1[] = tree.getByteCode(str);//из строки в байты
        System.out.println("total size after compression : "+arr1.length*8);


        File filenew = new File("D://1PROGA",file.getName() + ".archive");

        filenew.setWritable(true);////////////////////////////
        filenew.setReadable(false);
        FileOutputStream fil = new FileOutputStream(filenew);
        fil.write(arr1);
        filenew.createNewFile();//*/////////////////////////////////*/
    }
}
