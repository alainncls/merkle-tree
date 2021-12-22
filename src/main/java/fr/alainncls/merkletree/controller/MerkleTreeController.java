package fr.alainncls.merkletree.controller;

import fr.alainncls.merkletree.model.InputItems;
import fr.alainncls.merkletree.model.MerkleTree;
import fr.alainncls.merkletree.service.MerkleTreeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("merkletrees")
public class MerkleTreeController {

    private final MerkleTreeService merkleTreeService;

    @GetMapping("/")
    public List<MerkleTree> getAllMerkleTrees() {
        return merkleTreeService.getAllMerkleTrees();
    }

    @GetMapping("/{id}")
    public MerkleTree getMerkleTree(@PathVariable String id) {
        return merkleTreeService.getMerkleTree(id);
    }

    @GetMapping("/{id}/root")
    public Map<String, String> getMerkleTreeRoot(@PathVariable String id) {
        return Map.of("root", merkleTreeService.getMerkleTreeRoot(id));
    }

    @GetMapping("/{id}/height")
    public Map<String, Integer> getMerkleTreeHeight(@PathVariable String id) {
        return Map.of("height", merkleTreeService.getMerkleTreeHeight(id));
    }

    @GetMapping("/{id}/{level}")
    public List<String> getMerkleTreeLevel(@PathVariable String id, @PathVariable int level) {
        return merkleTreeService.getMerkleTreeLevel(id, level);
    }

    @PostMapping("/")
    public MerkleTree generateMerkleTree(@RequestBody InputItems inputItems) {
        return merkleTreeService.generateMerkleTree(inputItems);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMerkleTree(@PathVariable String id) {
        merkleTreeService.deleteMerkleTree(id);
    }

}
