package cn.keyi.bye.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.keyi.bye.model.Quotaformula;

public interface QuotaformulaDao extends JpaRepository<Quotaformula, Long> {
	
	// 根据材料名称，在材料特性表中查找指定材料特性数据
	Page<Quotaformula> findByMaterialNameContaining(String materialName, Pageable pageable);

}
