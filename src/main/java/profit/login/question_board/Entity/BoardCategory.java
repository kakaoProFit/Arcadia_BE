package profit.login.question_board.Entity;

public enum BoardCategory {
    FREE, INFORM, DIARY, QUESTION;
    //이거 대신에 지식인이랑 자유랑 일기로 바꾸면 될듯

    public static BoardCategory of(String category) {
        if (category.equalsIgnoreCase("free")) return BoardCategory.FREE;
        else if (category.equalsIgnoreCase("question")) return BoardCategory.QUESTION;
        else if (category.equalsIgnoreCase("inform")) return BoardCategory.INFORM;
        else if (category.equalsIgnoreCase("diary")) return BoardCategory.DIARY;
        else return null;
    }
}