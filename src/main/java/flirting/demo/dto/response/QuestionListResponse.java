package flirting.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import flirting.demo.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionListResponse {

    @JsonProperty("questionList")
    List<QuestionListData> questionList;

    public QuestionListResponse(List<Question> questions) {
        this.questionList = questions.stream().map(q -> QuestionListData.builder()
                .questionId(q.getId())
                .question(q.getContent())
                .build()
        ).toList();
    }

    public static class QuestionListData {
        // Todo: JsonProperty가 있어야 Bean Serializable... 에러가 안생김 -> 왜그런지 조사
        @JsonProperty("questionId")
        Long questionId;
        @JsonProperty("question")
        String question;

        @Builder
        public QuestionListData(Long questionId, String question) {
            this.questionId = questionId;
            this.question = question;
        }
    }
}
