package fr.alainncls.merkletree.service;

import fr.alainncls.merkletree.exception.MerkleTreeNotFoundException;
import fr.alainncls.merkletree.model.InputItems;
import fr.alainncls.merkletree.model.MerkleTree;
import fr.alainncls.merkletree.model.Node;
import fr.alainncls.merkletree.repository.MerkleTreeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MerkleTreeServiceTest {

    private final InputItems inputItems = InputItems.builder().items(List.of("ITEM_1", "ITEM_2", "ITEM_3", "ITEM_4")).build();
    final String ID = "ID_1";

    // Hashes computed via https://emn178.github.io/online-tools/sha256.html
    private final String HASH_ITEM_1 = "5224b22b2342302bf1b9f40af063d645959a5107f6c3955c8bce8890b945f68a";
    private final String HASH_ITEM_2 = "4633f0c4d24f5425a4cb1c539cdf4ca3b877ef42c3114dc75507b75ff3d2fe02";
    private final String HASH_ITEM_3 = "69982370a875b34e20833a9291bf55ec6af463aee128bd1732adb966bf1b2a85";
    private final String HASH_ITEM_4 = "b15af24ab0b8493abea3e8c2f01c7a51b721f8338f406478a37545a3c9bcd1b3";
    private final String HASH_ITEM_1_2 = "2b18e91cc1ace16cee6e3644819a528740e118e2649996bd6911bbfd80e58747";
    private final String HASH_ITEM_3_4 = "a56637dd5d3b1d8ac18e702ff50e0ae86470ef0d9aa68f72b34af4fb6b901693";
    private final String HASH_ITEM_1_2_3_4 = "0372ff538dc9f033eb1cd7610a75b92478bd37906705a006d07b26229ac3b0c7";

    private final Node node1 = Node.builder().hash(HASH_ITEM_1).build();
    private final Node node2 = Node.builder().hash(HASH_ITEM_2).build();
    private final Node node3 = Node.builder().hash(HASH_ITEM_3).build();
    private final Node node4 = Node.builder().hash(HASH_ITEM_4).build();
    private final Node node12 = Node.builder().hash(HASH_ITEM_1_2).leftNode(node1).rightNode(node2).build();
    private final Node node34 = Node.builder().hash(HASH_ITEM_3_4).leftNode(node3).rightNode(node4).build();
    private final Node nodeRoot = Node.builder().hash(HASH_ITEM_1_2_3_4).leftNode(node12).rightNode(node34).build();

    private final MerkleTree merkleTree = MerkleTree.builder().id("ID_1").children(nodeRoot).build();

    @Mock
    private MerkleTreeRepository merkleTreeRepository;

    @InjectMocks
    private MerkleTreeService merkleTreeService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    void getAllMerkleTrees() {
        List<MerkleTree> expectedMerkleTrees = List.of(merkleTree);

        when(merkleTreeRepository.findAll()).thenReturn(expectedMerkleTrees);

        List<MerkleTree> result = merkleTreeService.getAllMerkleTrees();

        assertThat(result).isNotNull().hasSize(expectedMerkleTrees.size()).isEqualTo(expectedMerkleTrees);
    }

    @Test
    void getMerkleTree() {
        when(merkleTreeRepository.findById(ID)).thenReturn(Optional.of(merkleTree));

        MerkleTree result = merkleTreeService.getMerkleTree(ID);

        assertThat(result).isNotNull().isEqualTo(merkleTree);
    }

    @Test
    void getMerkleTreeNotFound() {
        assertThrows(MerkleTreeNotFoundException.class, () -> merkleTreeService.getMerkleTree("UNKNOWN_ID"));
    }

    @Test
    void generateMerkleTree() {
        MerkleTree unsavedMerkleTree = MerkleTree.builder().children(merkleTree.getChildren()).build();

        when(merkleTreeRepository.save(unsavedMerkleTree)).thenReturn(merkleTree);

        merkleTreeService.generateMerkleTree(inputItems);

        verify(merkleTreeRepository, times(1)).save(unsavedMerkleTree);
    }

    @Test
    void deleteMerkleTree() {
        doNothing().when(merkleTreeRepository).deleteById(ID);

        merkleTreeService.deleteMerkleTree(ID);

        verify(merkleTreeRepository, times(1)).deleteById(ID);
    }

    @Test
    void getMerkleTreeRoot() {
        when(merkleTreeRepository.findById(ID)).thenReturn(Optional.of(merkleTree));

        String result = merkleTreeService.getMerkleTreeRoot(ID);

        assertThat(result).isNotNull().isEqualTo(HASH_ITEM_1_2_3_4);
    }

    @Test
    void getMerkleTreeHeight() {
        int HEIGHT = 3;

        when(merkleTreeRepository.findById(ID)).thenReturn(Optional.of(merkleTree));

        int result = merkleTreeService.getMerkleTreeHeight(ID);

        assertThat(result).isEqualTo(HEIGHT);
    }

    @Test
    void getMerkleTreeLevel() {
        when(merkleTreeRepository.findById(ID)).thenReturn(Optional.of(merkleTree));

        List<String> result = merkleTreeService.getMerkleTreeLevel(ID, 1);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0)).isEqualTo(HASH_ITEM_1_2);
        assertThat(result.get(1)).isEqualTo(HASH_ITEM_3_4);
    }
}
