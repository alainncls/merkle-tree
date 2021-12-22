package fr.alainncls.merkletree.repository;

import fr.alainncls.merkletree.model.MerkleTree;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerkleTreeRepository extends MongoRepository<MerkleTree, String> {
}
