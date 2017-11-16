package simonxyzjz.phdfms.mongo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import simonxyzjz.phdfms.mongo.domain.FileEntity;

public interface FileEntityRepository extends PagingAndSortingRepository<FileEntity, String> {

}
