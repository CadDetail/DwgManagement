package cn.keyi.bye.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="quotaformula")
public class Quotaformula implements Serializable {
	private static final long serialVersionUID = 3170879382568676697L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "formula_id")
	private Long formulaId;
	// 材料代码
	@Column(name = "material_code")
	private String materialCode;
	// 材料名称
	@Column(name = "material_name")
	private String materialName;
	// 单位
	@Column(name = "unit")
	private String unit;
	// 材料材质（密度）
	@Column(name = "feature")
	private Float feature;
	// 余量参数（除）
	@Column(name = "formula_factor")
	private Float formulaFactor;
	// 创建时间
	@Column(name = "create_time")
	private LocalDateTime createTime;
	// 创建人
	@Column(name = "create_by")
	private String createBy;
	// 修改时间
	@Column(name = "update_time")
	private LocalDateTime updateTime;
	// 修改人
	@Column(name = "update_by")
	private String updateBy;
	
	public Long getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Float getFeature() {
		return feature;
	}
	public void setFeature(Float feature) {
		this.feature = feature;
	}
	public Float getFormulaFactor() {
		return formulaFactor;
	}
	public void setFormulaFactor(Float formulaFactor) {
		this.formulaFactor = formulaFactor;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
