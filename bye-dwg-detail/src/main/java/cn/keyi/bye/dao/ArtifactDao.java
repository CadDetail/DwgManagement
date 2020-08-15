package cn.keyi.bye.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cn.keyi.bye.model.Artifact;

public interface ArtifactDao extends JpaRepository<Artifact, Long> {
	
	// 根据工件名称进行模糊查询，允许分页
	public Page<Artifact> findByArtifactNameContaining(String artifactName, Pageable pageable);
	// 根据工件类型进行查询，主要用于过滤顶层产品和下级零件
	public Page<Artifact> findByProductFlag(Short productFlag, Pageable pageable);
	// 根据工件名称进行模糊查询，附带工件类型
	public Page<Artifact> findByArtifactNameContainingAndProductFlag(String artifactName, Short productFlag, Pageable pageable);
	// 根据工件名称进行模糊查询，附带是否可分解
	public Page<Artifact> findByArtifactNameContainingAndCanBeSplit(String artifactName, Boolean canBeSplit, Pageable pageable);
	// 根据工件名称进行模糊查询，附带工件类型和是否可分解
	public Page<Artifact> findByArtifactNameContainingAndProductFlagAndCanBeSplit(String artifactName, Short productFlag, Boolean canBeSplit, Pageable pageable);
	// 根据图号查找某零件
	public List<Artifact> findByArtifactCode(String artifactCode);
}
