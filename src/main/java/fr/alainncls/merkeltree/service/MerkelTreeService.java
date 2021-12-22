package fr.alainncls.merkeltree.service;

import fr.alainncls.merkeltree.exception.HashException;
import fr.alainncls.merkeltree.exception.MerkelTreeLevelException;
import fr.alainncls.merkeltree.exception.MerkelTreeNotFoundException;
import fr.alainncls.merkeltree.model.InputItems;
import fr.alainncls.merkeltree.model.MerkelTree;
import fr.alainncls.merkeltree.model.Node;
import fr.alainncls.merkeltree.repository.MerkelTreeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MerkelTreeService {

    private static final String HASH_ALGORITHM = "SHA-256";

    private final MerkelTreeRepository merkelTreeRepository;

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public List<MerkelTree> getAllMerkelTrees() {
        return merkelTreeRepository.findAll();
    }

    public MerkelTree getMerkelTree(String id) {
        return merkelTreeRepository.findById(id).orElseThrow(MerkelTreeNotFoundException::new);
    }

    public MerkelTree generateMerkelTree(InputItems inputItems) {
        MerkelTree merkelTree = new MerkelTree();
        List<Node> leafNodes = inputItems.getItems().stream().map(item -> Node.builder().hash(hashItem(item)).build()).collect(Collectors.toList());
        List<Node> parents = new ArrayList<>();

        while (leafNodes.size() != 1) {
            int index = 0;
            int length = leafNodes.size();

            while (index < length) {
                Node leftChild = leafNodes.get(index);
                Node rightChild = null;

                if ((index + 1) < length) {
                    rightChild = leafNodes.get(index + 1);
                }

                // If a node has only one child, its hash is the same as its child’s.
                String newHash = rightChild != null ? hashItem(leftChild.getHash() + rightChild.getHash()) : leftChild.getHash();
                Node newNode = Node.builder().hash(newHash).leftNode(leftChild).rightNode(rightChild).build();

                parents.add(newNode);
                index += 2;
            }

            leafNodes = parents;
            parents = new ArrayList<>();
        }

        merkelTree.setChildren(leafNodes.get(0));

        return merkelTreeRepository.save(merkelTree);
    }

    public void deleteMerkelTree(String id) {
        merkelTreeRepository.deleteById(id);
    }

    public String getMerkelTreeRoot(String id) {
        MerkelTree merkelTree = merkelTreeRepository.findById(id).orElseThrow(MerkelTreeNotFoundException::new);
        return merkelTree.getChildren().getHash();
    }

    public int getMerkelTreeHeight(String id) {
        MerkelTree merkelTree = merkelTreeRepository.findById(id).orElseThrow(MerkelTreeNotFoundException::new);
        return getMerkelTreeHeight(merkelTree.getChildren());
    }

    public List<String> getMerkelTreeLevel(String id, int level) {
        MerkelTree merkelTree = merkelTreeRepository.findById(id).orElseThrow(MerkelTreeNotFoundException::new);
        int height = getMerkelTreeHeight(merkelTree.getChildren());

        if (level >= height) {
            throw new MerkelTreeLevelException();
        }

        return getNodesOnLevel(merkelTree.getChildren(), level, new ArrayList<>());
    }

    private String hashItem(String item) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            return bytesToHex(messageDigest.digest(item.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new HashException(HASH_ALGORITHM + " algorithm not found, can't hash anything");
        }
    }

    private int getMerkelTreeHeight(Node node) {
        if (node == null) {
            return 0;
        }

        int height = 0;
        Queue<Node> queue = new LinkedList();
        queue.add(node);

        while (true) {
            int nodeCount = queue.size();

            if (nodeCount == 0) {
                return height;
            }

            height++;

            while (nodeCount > 0) {
                // Remove all nodes of current level from the queue
                Node newNode = queue.peek();
                queue.remove();

                // Add all nodes of next level to queue
                if (newNode != null && newNode.getLeftNode() != null) {
                    queue.add(newNode.getLeftNode());
                }
                if (newNode != null && newNode.getRightNode() != null) {
                    queue.add(newNode.getRightNode());
                }

                nodeCount--;
            }
        }
    }

    private List<String> getNodesOnLevel(Node node, int level, List<String> list) {
        if (node == null) {
            return Collections.emptyList();
        }

        if (level == 0) {
            list.add(node.getHash());
        } else {
            getNodesOnLevel(node.getLeftNode(), level - 1, list);
            getNodesOnLevel(node.getRightNode(), level - 1, list);
        }

        return list;
    }
}
