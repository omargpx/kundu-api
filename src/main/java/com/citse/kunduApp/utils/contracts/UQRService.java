package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Assist;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.UserQuizResult;

import java.util.List;

/** User Quiz Result Service
 * Methods to use()
 * */
public interface UQRService {
    List<Member> getAllByGroup(int groupId);
    Assist save(UserQuizResult uqr,int memberId, int sessionId);
    Assist saveQRAssist(int sessionId, int memberId);
    UserQuizResult getById(int id);

}
