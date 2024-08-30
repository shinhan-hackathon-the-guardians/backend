package com.shinhan_hackathon.the_family_guardian.domain.chatbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan_hackathon.the_family_guardian.domain.chatbot.dto.request.ChatMessageRequest;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.dto.response.ChatHistoryResponseWrapper;
import com.shinhan_hackathon.the_family_guardian.domain.chatbot.service.ChatService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
	private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
	private final ChatService chatService;

	@PostMapping
	public Map<String, String> chat(@RequestBody ChatMessageRequest message) {
		Map<String, String> responses = new HashMap<>();
		String prompt =
			"너는 '더 패밀리 가디언'이라는 가족을 각종 금융 위협으로부터 보호하는 금융 안심 서비스 안에 있는 " +
				"인공지능 챗봇이고 이름은 '가디'야. '더 패밀리 가디언'은 서포터가 일정 금액(설정값)이상을 송금, 결제하려 할 때 가디언에게 " +
				"승인 요청이 가게 되고 일정 이상(설정값)의 가디언이 승인하면 송금 및 결제가 이루어지고, 거부하면 차단이 되도록 만들어" +
				"보호하고 있어. 그리고 서포터들의 사용 금액을 모니터링하며 관리해줄 수도 있어. 이제부터 금융상식이나 우리 '더 패밀리 가디언" +
				"에 대해서 질문을 할건데 너가 묻는 말에 친절하게 대답을 해줘. 그리고 너가 누구냐는 식의 질문 외에는 너의 소개는 하지 않아도 돼! 이제 질문할게! \n";
		String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(prompt + message);
		responses.put("Guardi의 응답", vertexAiGeminiResponse);

		chatService.saveMessage(getUserId(), message.message(), true);
		chatService.saveMessage(getUserId(), vertexAiGeminiResponse, false);
		return responses;
	}

	@GetMapping("/history/{userId}")
	public ChatHistoryResponseWrapper getChatHistory(@PathVariable Long userId) {
		ChatHistoryResponseWrapper responses = new ChatHistoryResponseWrapper(chatService.getChatHistory(userId));
		return responses;
	}

	private Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.user().getId();
	}
}
