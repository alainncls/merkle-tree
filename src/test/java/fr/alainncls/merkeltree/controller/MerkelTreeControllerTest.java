package fr.alainncls.merkeltree.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alainncls.merkeltree.exception.MerkelTreeNotFoundException;
import fr.alainncls.merkeltree.model.InputItems;
import fr.alainncls.merkeltree.model.MerkelTree;
import fr.alainncls.merkeltree.model.Node;
import fr.alainncls.merkeltree.service.MerkelTreeService;
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

@WebMvcTest(value = MerkelTreeController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class MerkelTreeControllerTest {

    private final String ROOT_HASH = "HASH_1_2_3_4";

    private final Node node1 = Node.builder().hash("HASH_1").build();
    private final Node node2 = Node.builder().hash("HASH_2").build();
    private final Node node3 = Node.builder().hash("HASH_3").build();
    private final Node node4 = Node.builder().hash("HASH_4").build();
    private final Node node11 = Node.builder().hash("HASH_1_2").leftNode(node1).rightNode(node2).build();
    private final Node node12 = Node.builder().hash("HASH_3_4").leftNode(node3).rightNode(node4).build();
    private final Node nodeRoot = Node.builder().hash(ROOT_HASH).leftNode(node11).rightNode(node12).build();

    private final MerkelTree merkelTree = MerkelTree.builder().id("ID_1").children(nodeRoot).build();

    @MockBean
    private MerkelTreeService merkelTreeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMerkelTrees() throws Exception {
        List<MerkelTree> expectedMerkelTrees = List.of(merkelTree);

        when(merkelTreeService.getAllMerkelTrees()).thenReturn(expectedMerkelTrees);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkeltrees/"))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("getAllMerkelTrees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMerkelTrees)))
                .andDo(document(
                        "getAllMerkelTrees",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        responseFields(
                                fieldWithPath("[]").description("The list of Merkel trees"),
                                fieldWithPath("[].id").description("The Merkel tree unique ID"),
                                fieldWithPath("[].children").description("First node of the tree"),
                                fieldWithPath("[].children.hash").description("The Merkel tree root hash, the \"Merkle root\""),
                                fieldWithPath("[].children.leftNode").description("Left node, containing itself a hash, a left and a right node, and so on"),
                                fieldWithPath("[].children.rightNode").description("Right node, containing itself a hash, a left and a right node, and so on"),
                                subsectionWithPath("[].children.leftNode").ignored(),
                                subsectionWithPath("[].children.rightNode").ignored())));
    }

    @Test
    void getMerkelTree() throws Exception {
        final String ID = "ID_1";

        when(merkelTreeService.getMerkelTree(ID)).thenReturn(merkelTree);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkeltrees/{id}", ID))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("getMerkelTree"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(merkelTree)))
                .andDo(document(
                        "getMerkelTree",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The requested Merkel tree ID")),
                        responseFields(
                                fieldWithPath("id").description("The Merkel tree unique ID"),
                                fieldWithPath("children").description("First node of the tree"),
                                fieldWithPath("children.hash").description("The Merkel tree root hash, the \"Merkle root\""),
                                fieldWithPath("children.leftNode").description("Left node, containing itself a hash, a left and a right node, and so on"),
                                fieldWithPath("children.rightNode").description("Right node, containing itself a hash, a left and a right node, and so on"),
                                subsectionWithPath("children.leftNode").ignored(),
                                subsectionWithPath("children.rightNode").ignored())));
    }

    @Test
    void getMerkelTreeNotFound() throws Exception {
        final String ID = "ID_3";

        when(merkelTreeService.getMerkelTree(ID)).thenThrow(new MerkelTreeNotFoundException());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkeltrees/{id}", ID))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("getMerkelTree"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document(
                        "getMerkelTreeNotFound",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse()));
    }

    @Test
    void getMerkelTreeRoot() throws Exception {
        final String ID = "ID_1";

        when(merkelTreeService.getMerkelTreeRoot(ID)).thenReturn(ROOT_HASH);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkeltrees/{id}/root", ID))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("getMerkelTreeRoot"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("root", ROOT_HASH))))
                .andDo(document(
                        "getMerkelTreeRoot",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The requested Merkel tree ID")),
                        responseFields(fieldWithPath("root").description("The Merkel root"))));
    }

    @Test
    void getMerkelTreeHeight() throws Exception {
        final String ID = "ID_1";
        final Integer HEIGHT = 3;

        when(merkelTreeService.getMerkelTreeHeight(ID)).thenReturn(HEIGHT);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkeltrees/{id}/height", ID))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("getMerkelTreeHeight"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("height", HEIGHT))))
                .andDo(document(
                        "getMerkelTreeHeight",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The requested Merkel tree ID")),
                        responseFields(fieldWithPath("height").description("The Merkel tree height"))));
    }

    @Test
    void getMerkelTreeLevel() throws Exception {
        final String ID = "ID_1";
        final int LEVEL = 2;
        final List<String> expectedHashes = List.of("HASH_1_2", "HASH_3_4");

        when(merkelTreeService.getMerkelTreeLevel(ID, LEVEL)).thenReturn(expectedHashes);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/merkeltrees/{id}/{level}", ID, LEVEL))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("getMerkelTreeLevel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedHashes)))
                .andDo(document(
                        "getMerkelTreeLevel",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(
                                parameterWithName("id").description("The requested Merkel tree ID"),
                                parameterWithName("level").description("The level in the requested Merkel tree")),
                        responseFields(fieldWithPath("[]").description("The list of nodes' hash on this level"))));
    }

    @Test
    void generateMerkelTree() throws Exception {
        InputItems inputItems = InputItems.builder().items(List.of("ITEM_1", "ITEM_2", "ITEM_3", "ITEM_4")).build();
        when(merkelTreeService.generateMerkelTree(inputItems)).thenReturn(merkelTree);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/merkeltrees/")
                        .content(objectMapper.writeValueAsString(inputItems))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("generateMerkelTree"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "generateMerkelTree",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        responseFields(
                                fieldWithPath("id").description("The Merkel tree unique ID"),
                                fieldWithPath("children").description("First node of the tree"),
                                fieldWithPath("children.hash").description("The Merkel tree root hash, the \"Merkle root\""),
                                fieldWithPath("children.leftNode").description("Left node, containing itself a hash, a left and a right node, and so on"),
                                fieldWithPath("children.rightNode").description("Right node, containing itself a hash, a left and a right node, and so on"),
                                subsectionWithPath("children.leftNode").ignored(),
                                subsectionWithPath("children.rightNode").ignored())));
    }

    @Test
    void deleteMerkelTree() throws Exception {
        final String ID = "ID_1";

        doNothing().when(merkelTreeService).deleteMerkelTree(ID);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/merkeltrees/{id}", ID)
                        .content(objectMapper.writeValueAsString(merkelTree))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(MerkelTreeController.class))
                .andExpect(handler().methodName("deleteMerkelTree"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(
                        "deleteMerkelTree",
                        ControllerTestUtils.preprocessRequest(),
                        ControllerTestUtils.preprocessResponse(),
                        pathParameters(parameterWithName("id").description("The ID of the Merkel tree to delete"))));
    }
}
