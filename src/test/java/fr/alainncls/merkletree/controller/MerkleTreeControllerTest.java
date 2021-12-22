package fr.alainncls.merkletree.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alainncls.merkletree.exception.MerkleTreeNotFoundException;
import fr.alainncls.merkletree.model.InputItems;
import fr.alainncls.merkletree.model.MerkleTree;
import fr.alainncls.merkletree.model.Node;
import fr.alainncls.merkletree.service.MerkleTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MerkleTreeController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class MerkleTreeControllerTest {

    private final String ROOT_HASH = "HASH_1_2_3_4";

    private final Node node1 = Node.builder().hash("HASH_1").build();
    private final Node node2 = Node.builder().hash("HASH_2").build();
    private final Node node3 = Node.builder().hash("HASH_3").build();
    private final Node node4 = Node.builder().hash("HASH_4").build();
    private final Node node11 = Node.builder().hash("HASH_1_2").leftNode(node1).rightNode(node2).build();
    private final Node node12 = Node.builder().hash("HASH_3_4").leftNode(node3).rightNode(node4).build();
    private final Node nodeRoot = Node.builder().hash(ROOT_HASH).leftNode(node11).rightNode(node12).build();

    private final MerkleTree merkleTree = MerkleTree.builder().id("ID_1").children(nodeRoot).build();

    @MockBean
    private MerkleTreeService merkleTreeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMerkleTrees() throws Exception {
        List<MerkleTree> expectedMerkleTrees = List.of(merkleTree);

        when(merkleTreeService.getAllMerkleTrees()).thenReturn(expectedMerkleTrees);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkletrees/"))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("getAllMerkleTrees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMerkleTrees)))
                .andDo(document(
                        "getAllMerkleTrees",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        responseFields(
                                fieldWithPath("[]").description("The list of Merkle trees"),
                                fieldWithPath("[].id").description("The Merkle tree unique ID"),
                                fieldWithPath("[].children").description("First node of the tree"),
                                fieldWithPath("[].children.hash").description("The Merkle tree root hash, the \"Merkle root\""),
                                fieldWithPath("[].children.leftNode").description("Left node, containing itself a hash, a left and a right node, and so on"),
                                fieldWithPath("[].children.rightNode").description("Right node, containing itself a hash, a left and a right node, and so on"),
                                subsectionWithPath("[].children.leftNode").ignored(),
                                subsectionWithPath("[].children.rightNode").ignored())));
    }

    @Test
    void getMerkleTree() throws Exception {
        final String ID = "ID_1";

        when(merkleTreeService.getMerkleTree(ID)).thenReturn(merkleTree);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkletrees/{id}", ID))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("getMerkleTree"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(merkleTree)))
                .andDo(document(
                        "getMerkleTree",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The requested Merkle tree ID")),
                        responseFields(
                                fieldWithPath("id").description("The Merkle tree unique ID"),
                                fieldWithPath("children").description("First node of the tree"),
                                fieldWithPath("children.hash").description("The Merkle tree root hash, the \"Merkle root\""),
                                fieldWithPath("children.leftNode").description("Left node, containing itself a hash, a left and a right node, and so on"),
                                fieldWithPath("children.rightNode").description("Right node, containing itself a hash, a left and a right node, and so on"),
                                subsectionWithPath("children.leftNode").ignored(),
                                subsectionWithPath("children.rightNode").ignored())));
    }

    @Test
    void getMerkleTreeNotFound() throws Exception {
        final String ID = "ID_3";

        when(merkleTreeService.getMerkleTree(ID)).thenThrow(new MerkleTreeNotFoundException());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkletrees/{id}", ID))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("getMerkleTree"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document(
                        "getMerkleTreeNotFound",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse()));
    }

    @Test
    void getMerkleTreeRoot() throws Exception {
        final String ID = "ID_1";

        when(merkleTreeService.getMerkleTreeRoot(ID)).thenReturn(ROOT_HASH);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkletrees/{id}/root", ID))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("getMerkleTreeRoot"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("root", ROOT_HASH))))
                .andDo(document(
                        "getMerkleTreeRoot",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The requested Merkle tree ID")),
                        responseFields(fieldWithPath("root").description("The Merkle root"))));
    }

    @Test
    void getMerkleTreeHeight() throws Exception {
        final String ID = "ID_1";
        final Integer HEIGHT = 3;

        when(merkleTreeService.getMerkleTreeHeight(ID)).thenReturn(HEIGHT);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkletrees/{id}/height", ID))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("getMerkleTreeHeight"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("height", HEIGHT))))
                .andDo(document(
                        "getMerkleTreeHeight",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The requested Merkle tree ID")),
                        responseFields(fieldWithPath("height").description("The Merkle tree height"))));
    }

    @Test
    void getMerkleTreeLevel() throws Exception {
        final String ID = "ID_1";
        final int LEVEL = 2;
        final List<String> expectedHashes = List.of("HASH_1_2", "HASH_3_4");

        when(merkleTreeService.getMerkleTreeLevel(ID, LEVEL)).thenReturn(expectedHashes);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkletrees/{id}/{level}", ID, LEVEL))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("getMerkleTreeLevel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedHashes)))
                .andDo(document(
                        "getMerkleTreeLevel",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(
                                parameterWithName("id").description("The requested Merkle tree ID"),
                                parameterWithName("level").description("The level in the requested Merkle tree")),
                        responseFields(fieldWithPath("[]").description("The list of nodes' hash on this level"))));
    }

    @Test
    void generateMerkleTree() throws Exception {
        InputItems inputItems = InputItems.builder().items(List.of("ITEM_1", "ITEM_2", "ITEM_3", "ITEM_4")).build();
        when(merkleTreeService.generateMerkleTree(inputItems)).thenReturn(merkleTree);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/merkletrees/")
                        .content(objectMapper.writeValueAsString(inputItems))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("generateMerkleTree"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "generateMerkleTree",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        requestFields(fieldWithPath("items").description("The list of source items for the Merkle tree")),
                        responseFields(
                                fieldWithPath("id").description("The Merkle tree unique ID"),
                                fieldWithPath("children").description("First node of the tree"),
                                fieldWithPath("children.hash").description("The Merkle tree root hash, the \"Merkle root\""),
                                fieldWithPath("children.leftNode").description("Left node, containing itself a hash, a left and a right node, and so on"),
                                fieldWithPath("children.rightNode").description("Right node, containing itself a hash, a left and a right node, and so on"),
                                subsectionWithPath("children.leftNode").ignored(),
                                subsectionWithPath("children.rightNode").ignored())));
    }

    @Test
    void deleteMerkleTree() throws Exception {
        final String ID = "ID_1";

        doNothing().when(merkleTreeService).deleteMerkleTree(ID);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/merkletrees/{id}", ID)
                        .content(objectMapper.writeValueAsString(merkleTree))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(MerkleTreeController.class))
                .andExpect(handler().methodName("deleteMerkleTree"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(
                        "deleteMerkleTree",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The ID of the Merkle tree to delete"))));
    }
}
