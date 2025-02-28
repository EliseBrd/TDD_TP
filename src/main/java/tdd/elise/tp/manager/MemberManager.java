package tdd.elise.tp.manager;

import tdd.elise.tp.exceptions.InvalidBirthdateException;
import tdd.elise.tp.exceptions.InvalidEmailException;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.service.MemberDataService;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void validateEmail(Member member) {
        String email = member.getEmail();
        // Expression régulière pour un email simple
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (email == null || !matcher.matches()) {
            throw new InvalidEmailException();
        }
    }
}
