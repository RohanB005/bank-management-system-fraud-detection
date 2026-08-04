using FraudDetectionService.AI.Clients;
using FraudDetectionService.AI.Interfaces;
using FraudDetectionService.Configuration;
using FraudDetectionService.Data;
using FraudDetectionService.Services;
using FraudDetectionService.Services.Interfaces;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

var connectionString = builder.Configuration.GetConnectionString("FraudDb");

builder.Services.Configure<GeminiSettings>(
    builder.Configuration.GetSection("Gemini"));

builder.Services.AddHttpClient();

builder.Services.AddScoped<IGeminiClient, GeminiClient>();

builder.Services.AddScoped<IAIExplanationService, AIExplanationService>();

builder.Services.AddScoped<IChatAssistantService, ChatAssistantService>();

builder.Services.AddDbContext<FraudDbContext>(options =>
    options.UseMySql(
        connectionString,
        ServerVersion.AutoDetect(connectionString)
    ));

builder.Services.AddScoped<IFraudService, FraudService>();

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.MapControllers();

app.Run();