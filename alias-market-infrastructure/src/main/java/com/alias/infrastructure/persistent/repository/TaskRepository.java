package com.alias.infrastructure.persistent.repository;

import org.springframework.stereotype.Repository;
import com.alias.domain.task.model.entity.TaskEntity;
import com.alias.domain.task.repository.ITaskRepository;
import com.alias.infrastructure.event.EventPublisher;
import com.alias.infrastructure.persistent.dao.ITaskDao;
import com.alias.infrastructure.persistent.po.TaskPO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 任务服务仓储实现
 * @create 2024-04-06 10:57
 */
@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<TaskPO> tasks = taskDao.queryNoSendMessageTaskList();

        List<TaskEntity> taskEntities = new ArrayList<>(tasks.size());
        for (TaskPO task : tasks) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(task.getUserId());
            taskEntity.setTopic(task.getTopic());
            taskEntity.setMessageId(task.getMessageId());
            taskEntity.setMessage(task.getMessage());
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        TaskPO taskReq = new TaskPO();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageCompleted(taskReq);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        TaskPO taskReq = new TaskPO();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageFail(taskReq);
    }

}
