package profit.login.question_board.Entity;

public enum BoardCategory {
    FREE, GREETING, GOLD;
    //이거 대신에 지식인이랑 자유랑 일기로 바꾸면 될듯

    public static BoardCategory of(String category) {
        if (category.equalsIgnoreCase("free")) return BoardCategory.FREE;
        else if (category.equalsIgnoreCase("greeting")) return BoardCategory.GREETING;
        else if (category.equalsIgnoreCase("gold")) return BoardCategory.GOLD;
        else return null;
    }
}