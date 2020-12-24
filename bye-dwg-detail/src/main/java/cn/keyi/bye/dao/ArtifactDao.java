package cn.keyi.bye.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cn.keyi.bye.model.Artifact;

public interface ArtifactDao extends JpaRepository<Artifact, Long> {
	
	// 根据工件名称进行模糊查询，允许分页
	Page<Artifact> findByArtifactCodeContainingOrArtifactNameContaining(String artifactCode, String artifactName, Pageable pageable);
	// 根据工件名称进行模糊查询，附带工件类型
	Page<Artifact> findByArtifactCodeContainingAndProductFlagOrArtifactNameContainingAndProductFlag(String artifactCode, Short productFlag1, String artifactName, Short productFlag2, Pageable pageable);	
	// 根据图号查找某零件
	List<Artifact> findByArtifactCode(String artifactCode);
	// 统计数据库中产品或零件的数量
	Long countByProductFlag(Short productFlag);
	// 根据工件名称和材料代码查找某工件
	List<Artifact> findByArtifactNameAndMaterialCode(String artifactName, String materialCode);
	// 根据工件图号列表查询工件
	List<Artifact> findByArtifactCodeIn(List<String> artifactCodes);
	
}
