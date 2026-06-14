# 无人机信息管理 API 文档

**基础路径**：`/api/uav`
**认证要求**：请求需携带会话 Cookie（`JSESSIONID`），依赖 Shiro 拦截。

---

## 1. 分页查询无人机列表

- **URL**: `/list`
- **Method**: `GET`
- **参数（Query）**:
  - `model` (String, 可选)：型号模糊查询
  - `uavCode` (String, 可选)：注册编号精确查询
  - `status` (Integer, 可选)：状态（1-正常，0-停用）
  - `pageNum` (Integer, 默认 1)：页码
  - `pageSize` (Integer, 默认 10)：每页条数

**请求示例**：
`GET /api/uav/list?model=DJI&pageNum=1&pageSize=10`

**成功返回示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 1,
    "pages": 1,
    "pageNum": 1,
    "pageSize": 10,
    "rows": [
      {
        "id": 1,
        "uavCode": "UAV-001",
        "model": "DJI Mini 3",
        "manufacturer": "DJI",
        "maxPayload": 0.5,
        "status": 1,
        "aiGenerated": 0,
        "createdAt": "2026-04-18 10:00:00"
      }
    ]
  }
}
```

---

## 2. 查询无人机详情

- **URL**: `/{id}`
- **Method**: `GET`
- **路径参数**:
  - `id` (Long, 必填)：无人机主键

**成功返回示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "uavCode": "UAV-001",
    "model": "DJI Mini 3"
  }
}
```

---

## 3. 新增无人机

- **URL**: `/`
- **Method**: `POST`
- **请求体（JSON）**:
  - `uavCode` (String, 必填)：注册编号（唯一，最大64字）
  - `model` (String, 必填)：型号（最大100字）
  - `manufacturer` (String, 可选)
  - `maxPayload` (Double, 可选)
  - `maxAltitude` (Integer, 可选)
  - `maxFlightTime` (Integer, 可选)
  - `maxSpeed` (Double, 可选)
  - `wingspan` (Double, 可选)
  - `weight` (Double, 可选)
  - `remark` (String, 可选)

**请求示例**：
```json
{
  "uavCode": "UAV-TEST",
  "model": "测试极夜"
}
```

**成功返回示例**：
```json
{
  "code": 200,
  "msg": "新增成功",
  "data": null
}
```

---

## 4. 修改无人机

- **URL**: `/{id}`
- **Method**: `PUT`
- **路径参数**:
  - `id` (Long, 必填)：无人机主键
- **请求体（JSON）**:
  - 与新增参数类似，且不包含 `uavCode`，新增了 `status`。

**请求示例**：
```json
{
  "id": 1,
  "model": "新型号",
  "status": 0
}
```

**成功返回示例**：
```json
{
  "code": 200,
  "msg": "修改成功",
  "data": null
}
```

---

## 5. 逻辑删除无人机

- **URL**: `/{id}`
- **Method**: `DELETE`
- **路径参数**:
  - `id` (Long, 必填)：无人机主键

**成功返回示例**：
```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

---

## 6. AI 属性生成（辅助）

- **URL**: `/ai-generate`
- **Method**: `GET`
- **参数（Query）**:
  - `model` (String, 必填)：模型名/类型，如 `工业级`

**成功返回示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "model": "工业级",
    "manufacturer": "AI 自动生成",
    "maxPayload": 15.5,
    "maxAltitude": 4000,
    "maxFlightTime": 65,
    "aiGenerated": 1,
    "remark": "由 AI 规则引擎自动生成，等级：工业级"
  }
}
```
