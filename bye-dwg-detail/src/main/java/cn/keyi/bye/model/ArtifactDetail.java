package cn.keyi.bye.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="artifactdetail")
public class ArtifactDetail implements Serializable  {
	private static final long serialVersionUID = 780143142129176138L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detailId")
	private Long detailId;
	// 数量
	@Column(name = "number")
	private Integer number = 1;
	// 明细表中的此工件是否需要继续分解（获取其下一级明细）
	@Column(name = "needSplit")
	private Boolean needSplit = Boolean.TRUE;
	// 参考尺寸（会签） 
	@Column(name = "dimension")
	private String dimension;
	// 单位（会签）
	@Column(name = "unit")
	private String unit;
	// 定额（会签）
	@Column(name = "quota")
	private String quota;
	// 工序（会签）
	@Column(name = "workingSteps")
	private String workingSteps;
	// 分类标识（会签）
	@Column(name = "classificationSign")
	private String classificationSign;
	// 处理标志（会签）
	@Column(name = "processSign")
	private String processSign;
	// 会签员
	@Column(name = "inspector")
	private String inspector;
	// 备注
	@Column(name = "detailMemo")
	private String detailMemo;
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
	// 父工件
	@ManyToOne
	@JoinColumn(name="parentArtifactId")
	//@JsonIgnore
	private Artifact master;
	// 子工件
	@ManyToOne
	@JoinColumn(name="artifactId")
	//@JsonIgnore
	private Artifact slave;
	
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Boolean getNeedSplit() {
		return needSplit;
	}
	public void setNeedSplit(Boolean needSplit) {
		this.needSplit = needSplit;
	}
	public String getDimension() {
		return dimension;
	}
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	public String getWorkingSteps() {
		return workingSteps;
	}
	public void setWorkingSteps(String workingSteps) {
		this.workingSteps = workingSteps;
	}
	public String getClassificationSign() {
		return classificationSign;
	}
	public void setClassificationSign(String classificationSign) {
		this.classificationSign = classificationSign;
	}
	public String getProcessSign() {
		return processSign;
	}
	public void setProcessSign(String processSign) {
		this.processSign = processSign;
	}
	public String getInspector() {
		return inspector;
	}
	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	public String getDetailMemo() {
		return detailMemo;
	}
	public void setDetailMemo(String detailMemo) {
		this.detailMemo = detailMemo;
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
	public Artifact getMaster() {
		return master;
	}
	public void setMaster(Artifact master) {
		this.master = master;
	}
	public Artifact getSlave() {
		return slave;
	}
	public void setSlave(Artifact slave) {
		this.slave = slave;
	}	
	
}
