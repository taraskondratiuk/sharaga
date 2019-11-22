package sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Tree implements Comparable<Tree>{


    Node root;

    public Tree(Node root) {
        this.root = root;
    }
    public static Tree buildHuffmanTree(HashMap<Byte, Integer> charFrequencies) {

        PriorityQueue<Tree> trees = new PriorityQueue<>();

        for (Map.Entry<Byte, Integer> i: charFrequencies.entrySet()) {

            trees.offer(new Tree(new Node(i.getValue(), i.getKey())));

        }

        while (trees.size() > 1) {
            // 4. - 5.
            Tree a = trees.poll();
            Tree b = trees.poll();
            trees.offer(new Tree(new Node(a, b)));
        }

        return trees.poll();
    }

    public String incode(byte [] file) {
        Map codes = codeTable();
        StringBuilder result = new StringBuilder();


        for (int i = 0; i < file.length; i++) {
            result.append(codes.get(file[i]));
        }
        return result.toString();
    }

    private Map codeTable() {
        Map<Byte, String> codeTable = new HashMap<>();
        codeTable(root, new StringBuilder(), codeTable);
        return codeTable;
    }

    private void codeTable(Node node, StringBuilder code, Map<Byte, String> codeTable) {
        if (node.byteVal != null) {
            codeTable.put(node.byteVal, code.toString());
            return;
        }
        codeTable(node.leftChild, code.append('0'), codeTable);
        code.deleteCharAt(code.length() - 1);
        codeTable(node.rightChild, code.append('1'), codeTable);
        code.deleteCharAt(code.length() - 1);
    }

    private void printCodes(Node current, StringBuilder code) {

        if (current.byteVal != null) {

            System.out.println(" "+current.byteVal + "      \t " + current.frequency + "      \t" + code);
        } else {

            printCodes(current.leftChild, code.append('0'));
            code.deleteCharAt(code.length() - 1);

            printCodes(current.rightChild, code.append('1'));
            code.deleteCharAt(code.length() - 1);

        }
    }


    public void printCodes() throws CloneNotSupportedException, IOException {

        System.out.println("char\t frequency\t binary code");
        printCodes(root, new StringBuilder());
    }

    public StringBuilder inOrderCode(Node root) {

        StringBuilder code = new StringBuilder();
        do {

            if (root.leftChild!=null && !root.leftChild.mark) {
                //System.out.println("232");
                root.leftChild.prev = root;
                root = root.leftChild;
                /*Node rootOld = root.clone();
                root = root.leftChild;
                root.prev = rootOld;*/
                root.mark = true;
                if(root.byteVal!=null) {
                    code.append("1");
                    code.append(Node.getBinary(root));
                    //System.out.println("Binary "+Node.getBinary(root));
                    //code.append(root.byteVal);
                    root = root.prev;
                    continue;
                    //System.out.println(code);
                    //System.out.println("1");
                    //root = root.rightChild;
                    //System.out.println("2");
                    //System.out.println(root.character);
                    //System.out.println("3");
                }
                code.append("0");
                //System.out.println(code);
                //continue;
            }else if(root.rightChild!=null && !root.rightChild.mark) {

                root.rightChild.prev = root;
                root = root.rightChild;
                /*Node rootOld = root.clone();
                root = root.rightChild;
                //System.out.println("sperma");
                root.prev = rootOld;*/
                root.mark = true;
                if(root.byteVal!=null) {
                    code.append("1");
                    //code.append(root.byteVal);
                    code.append(Node.getBinary(root));
                    //System.out.println("Binary "+Node.getBinary(root));
                    root = root.prev;
                    continue;
                    //    System.out.println(code);

                }
                code.append("0");
                //System.out.println(code);
                //continue;
            } else {
                if (root.prev !=null) {
                    root = root.prev;
                    code.append("0");
                    //System.out.println(code);
                }
                else

                    return code;
            }
            //if (root == clone)
            //  calc++;
        } while (true);



        //if (root == null)
        //  return;
            /*if (leftChild != null) {
                code.append("0");
                leftChild.inOrderCode(code);
            }
            if (this.character != null) {
                code.deleteCharAt(code.length() - 1);
                code.append("1");
                code.append(this.character);
            }
            //if (this.character == null) {
            //    code.append("0");
            // }
            if (rightChild != null) {
                code.append("0");
                rightChild.inOrderCode(code);
                //code.deleteCharAt(code.length()-1);
                code.append("0");
            }*/
    }


    public StringBuilder getFinalFileCode(int treeLen, String file) {
        StringBuilder finFile = new StringBuilder();
        int lenFile = file.length();
        finFile.append(file);
        for (int i = 0;i < 8 - ((file.length() +treeLen) % 8);i++) {
            finFile.append("0");
        }

        StringBuilder lenFileStr = new StringBuilder();
        lenFileStr.append(Integer.toBinaryString(lenFile));
        StringBuilder finLenFileStr = new StringBuilder();
        for (int i = 0; i < 32 - lenFileStr.length(); i++) {
            finLenFileStr.append("0");
        }
        finLenFileStr.append(lenFileStr);
        finFile.append(finLenFileStr);
        return finFile;

    }


    public byte[] getByteCode(StringBuilder readyFile) {
        byte arr[] = new byte[readyFile.length()/8];
        int j =0;
        for (int i = 0; i < readyFile.length(); i+=8) {
            if (readyFile.substring(i,i+8).charAt(0) == '0')
                arr[j] = Byte.parseByte(readyFile.substring(i, i + 8), 2);
            else {
                if (readyFile.substring(i, i + 8).equals("10000000"))
                    arr[j] = -128;
                else {
                    StringBuilder stringBuilder = new StringBuilder(readyFile.substring(i, i+8));
                    stringBuilder.setCharAt(0, '0');
                    arr[j] =(byte)(0 - Byte.parseByte(String.valueOf(stringBuilder), 2));
                }

            }


            j++;
        }
        return arr;
    }



    @Override
    public int compareTo(Tree tree) {
        return root.frequency - tree.root.frequency;
    }
}
