import java.util.Random;

class Node {
    int value;
    int height;
    Node left;
    Node right;

    public Node(int value) {
        this.value = value;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

class AVLTree {
    Node root;

    public AVLTree() {
        this.root = null;
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int balanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;
        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;
        return newRoot;
    }

    public void insert(int value) {
        root = insertNode(root, value);
    }

    private Node insertNode(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.value) {
            node.left = insertNode(node.left, value);
        } else if (value > node.value) {
            node.right = insertNode(node.right, value);
        } else {
            return node;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int balance = balanceFactor(node);
        if (balance > 1 && value < node.left.value) {
            return rotateRight(node);
        }
        if (balance < -1 && value > node.right.value) {
            return rotateLeft(node);
        }
        if (balance > 1 && value > node.left.value) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && value < node.right.value) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    public void printTree() {
        printNode(root);
    }

    private void printNode(Node node) {
        if (node != null) {
            printNode(node.left);
            System.out.println("Valor: " + node.value + ", Fator de Balanceamento: " + balanceFactor(node));
            printNode(node.right);
        }
    }

    public void remove(int value) {
        root = removeNode(root, value);
    }

    private Node removeNode(Node node, int value) {
        if (node == null) {
            return node;
        }
        if (value < node.value) {
            node.left = removeNode(node.left, value);
        } else if (value > node.value) {
            node.right = removeNode(node.right, value);
        } else {
            if (node.left == null || node.right == null) {
                Node temp = null;
                if (temp == node.left) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                Node temp = minValueNode(node.right);
                node.value = temp.value;
                node.right = removeNode(node.right, temp.value);
            }
        }
        if (node == null) {
            return node;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int balance = balanceFactor(node);
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
}

public class ArvoreAVL {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        Random random = new Random();

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            int randomNumber = random.nextInt(1001) - 500;
            tree.insert(randomNumber);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Converter para milissegundos
        System.out.println("Tempo necessário para inserir 100 números aleatórios: " + duration + " ms");

        System.out.println("Árvore AVL após a inserção de 100 números aleatórios:");
        tree.printTree();

        startTime = System.nanoTime();
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(1001) - 500;
            tree.remove(randomNumber);
        }
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000; // Converter para milissegundos
        System.out.println("Tempo necessário para remover 20 números aleatórios: " + duration + " ms");

        System.out.println("Árvore AVL após a remoção de 20 números aleatórios:");
        tree.printTree();
    }
}
