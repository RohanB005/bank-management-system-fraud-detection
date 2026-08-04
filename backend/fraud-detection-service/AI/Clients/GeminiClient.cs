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

            var response = await _httpClient.PostAsJsonAsync(url, request);

            var responseText = await response.Content.ReadAsStringAsync();

            Console.WriteLine($"Status Code: {(int)response.StatusCode}");
            Console.WriteLine(responseText);

            if (!response.IsSuccessStatusCode)
            {
                throw new Exception($"Gemini API Error:\n{responseText}");
            }

            var result = await response.Content.ReadFromJsonAsync<GeminiResponse>();

            if (result == null ||
                result.Candidates == null ||
                result.Candidates.Count == 0 ||
                result.Candidates[0].Content == null ||
                result.Candidates[0].Content.Parts == null ||
                result.Candidates[0].Content.Parts.Count == 0)
            {
                throw new Exception("Gemini returned an empty response.");
            }

            return result.Candidates[0].Content.Parts[0].Text;
        }
    }
}