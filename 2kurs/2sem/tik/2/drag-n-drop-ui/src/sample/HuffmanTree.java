package sample;

import sample.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree implements Comparable<HuffmanTree>{


    Node root;

    public HuffmanTree(Node root) {
        this.root = root;
    }
    public static HuffmanTree buildHuffmanTree(HashMap<Byte, Integer> charFrequencies) {

        PriorityQueue<HuffmanTree> trees = new PriorityQueue<>();
        // 1. - 3.
        for (Map.Entry<Byte, Integer> i: charFrequencies.entrySet()) {

            trees.offer(new HuffmanTree(new Node(i.getValue(), i.getKey())));

        }
        // 6. пока в очереди не останется только одно дерево
        while (trees.size() > 1) {
            // 4. - 5.
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
            trees.offer(new HuffmanTree(new Node(a, b)));
        }
        /*Когда в очереди останется всего одно дерево, оно представляет собой дерево Хаффмана. */
        return trees.poll();
    }

    public String incode(byte [] file) {
        /*Для кодирования сообщения необходимо создать кодовую таблицу,
         * в которой для каждого символа приводится соответствующий код Хаффмана.*/
        Map codes = codeTable();
        StringBuilder result = new StringBuilder();
        /*Далее коды Хаффмана раз за разом присоединяются к кодированному сообщению,
         * пока оно не будет завершено.*/
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
        //если узел листовой
        if (current.byteVal != null) {
            // выводим символ, частоту и код пути от корня до текущкго узла
            System.out.println(" "+current.byteVal + "      \t " + current.frequency + "      \t" + code);
        } else {
            // обходим левое поддерево
            printCodes(current.leftChild, code.append('0'));
            code.deleteCharAt(code.length() - 1);
            // обходим правое поддерево
            printCodes(current.rightChild, code.append('1'));
            code.deleteCharAt(code.length() - 1);

        }
    }


    public void printCodes() throws CloneNotSupportedException, IOException {

        System.out.println("char\t frequency\t binary code");
        printCodes(root, new StringBuilder());
    }

    public StringBuilder inOrderCode(StringBuilder code, Node root) throws CloneNotSupportedException {


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
    public int compareTo(HuffmanTree tree) {
        return root.frequency - tree.root.frequency;
    }
}
