package fr.alainncls.merkeltree.repository;

import fr.alainncls.merkeltree.model.MerkelTree;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerkelTreeRepository extends MongoRepository<MerkelTree, String> {
}
