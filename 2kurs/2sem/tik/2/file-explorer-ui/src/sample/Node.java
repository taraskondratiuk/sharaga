package sample;

public class Node implements Cloneable{
    // Частота символа
    Integer frequency;
    // Символ
    Byte byteVal;
    // Левый потомок узла
    Node leftChild;
    // Правый потомок узла
    Node rightChild;
    boolean mark = false;
    Node prev;
    public Node(Integer frequency, Byte byteVal) {
        this.frequency = frequency;
        this.byteVal = byteVal;
    }

    public Node(Tree left, Tree right) {
        frequency = left.root.frequency + right.root.frequency;
        leftChild = left.root;
        rightChild = right.root;
    }

    public Node clone() throws CloneNotSupportedException{

        return (Node) super.clone();
    }

    /*public void check1() throws CloneNotSupportedException {
            Node test = new Node(0,null);
            Node test2 = inOrderDecode(new StringBuilder("01f1u0001t1i001s1r00"),test);
            Inorder(test2);
    }*/


    @Override
    public String toString() {
        return "[id=" + frequency + ", data =" + byteVal + "]";
    }

    public static String getBinary(Node node) {

        StringBuilder m = new StringBuilder();
        if (node.byteVal >= 0) {
            String x = Integer.toBinaryString(node.byteVal);
            int num = 8 - x.length();

            while (num > 0) {
                m.append("0");
                num--;
            }
            m.append(x);
        }
        else if (node.byteVal > -128) {
            String x = Integer.toBinaryString(0 - node.byteVal);
            int num = 8 - x.length();
            while (num > 0) {
                m.append("0");
                num--;
            }
            m.append(x);
            m.setCharAt(0, '1');

        }
        else {
            m = new StringBuilder("10000000");
        }
        //System.out.println("m : "+m);
        return m.toString();
    }

}