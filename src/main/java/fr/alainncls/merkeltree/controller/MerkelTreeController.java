package fr.alainncls.merkeltree.controller;

import fr.alainncls.merkeltree.model.InputItems;
import fr.alainncls.merkeltree.model.MerkelTree;
import fr.alainncls.merkeltree.service.MerkelTreeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("merkeltrees")
public class MerkelTreeController {

    private final MerkelTreeService merkelTreeService;

    @GetMapping("/")
    public List<MerkelTree> getAllMerkelTrees() {
        return merkelTreeService.getAllMerkelTrees();
    }

    @GetMapping("/{id}")
    public MerkelTree getMerkelTree(@PathVariable String id) {
        return merkelTreeService.getMerkelTree(id);
    }

    @GetMapping("/{id}/root")
    public Map<String, String> getMerkelTreeRoot(@PathVariable String id) {
        return Map.of("root", merkelTreeService.getMerkelTreeRoot(id));
    }

    @GetMapping("/{id}/height")
    public Map<String, Integer> getMerkelTreeHeight(@PathVariable String id) {
        return Map.of("height", merkelTreeService.getMerkelTreeHeight(id));
    }

    @GetMapping("/{id}/{level}")
    public List<String> getMerkelTreeLevel(@PathVariable String id, @PathVariable int level) {
        return merkelTreeService.getMerkelTreeLevel(id, level);
    }

    @PostMapping("/")
    public MerkelTree generateMerkelTree(@RequestBody InputItems inputItems) {
        return merkelTreeService.generateMerkelTree(inputItems);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMerkelTree(@PathVariable String id) {
        merkelTreeService.deleteMerkelTree(id);
    }

}
