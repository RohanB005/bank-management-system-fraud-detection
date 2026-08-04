using System.Net.Http.Json;
using FraudDetectionService.AI.Interfaces;
using FraudDetectionService.AI.Models;
using FraudDetectionService.Configuration;
using Microsoft.Extensions.Options;

namespace FraudDetectionService.AI.Clients
{
    public class GeminiClient : IGeminiClient
    {
        private readonly HttpClient _httpClient;
        private readonly GeminiSettings _settings;

        public GeminiClient(
            HttpClient httpClient,
            IOptions<GeminiSettings> options)
        {
            _httpClient = httpClient;
            _settings = options.Value;
        }

        public async Task<string> GenerateContentAsync(string prompt)
        {
            var request = new GeminiRequest
            {
                Contents = new List<Content>
                {
                    new Content
                    {
                        Parts = new List<Part>
                        {
                            new Part
                            {
                                Text = prompt
                            }
                        }
                    }
                }
            };

            string url =
                $"{_settings.BaseUrl}/v1beta/models/{_settings.Model}:generateContent?key={_settings.ApiKey}";

            Console.WriteLine($"Calling Gemini API:");
            Console.WriteLine(url);

            try
            {
                var response = await _httpClient.PostAsJsonAsync(url, request);
                var responseText = await response.Content.ReadAsStringAsync();

                Console.WriteLine($"Status Code: {(int)response.StatusCode}");

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine($"Gemini API Error Response: {responseText}");
                    return GetFallbackResponse(prompt);
                }

                var result = await response.Content.ReadFromJsonAsync<GeminiResponse>();

                if (result?.Candidates != null &&
                    result.Candidates.Count > 0 &&
                    result.Candidates[0].Content?.Parts != null &&
                    result.Candidates[0].Content.Parts.Count > 0 &&
                    !string.IsNullOrWhiteSpace(result.Candidates[0].Content.Parts[0].Text))
                {
                    return result.Candidates[0].Content.Parts[0].Text;
                }

                return GetFallbackResponse(prompt);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"GeminiClient Exception: {ex.Message}");
                return GetFallbackResponse(prompt);
            }
        }

        private static string GetFallbackResponse(string prompt)
        {
            if (prompt.Contains("Fraud Assessment", StringComparison.OrdinalIgnoreCase))
            {
                return "AI Security Notice: This transaction has been flagged due to high risk indicators including unusual transfer amount or geographical velocity mismatch. Standard fraud prevention protocols recommend identity verification before proceeding.";
            }

            return "Welcome to Secure Bank AI Assistant! I can help you understand account security, transaction safety, fraud alerts, and transfer limits. How can I assist you with your banking today?";
        }
    }
}