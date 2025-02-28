package tdd.elise.tp.manager;

import tdd.elise.tp.exceptions.InvalidBirthdateException;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.service.MemberDataService;

import java.util.Date;

public class MemberManager {
    private final MemberDataService databaseService;
    private final MemberDataService webService;

    public MemberManager(MemberDataService databaseService, MemberDataService webService) {
        this.databaseService = databaseService;
        this.webService = webService;
    }

    public void validateBirthdate(Member member) {
        Date birthdate = member.getBirthDate();
        if (birthdate != null && birthdate.after(new Date())) {
            throw new InvalidBirthdateException();
        }
    }
}
