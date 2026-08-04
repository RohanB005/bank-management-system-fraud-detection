# Stage 1: Build & Publish stage using .NET 8 SDK
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build

WORKDIR /src

# Copy project file and restore dependencies to cache layer
COPY ["FraudDetectionService.csproj", "./"]
RUN dotnet restore "FraudDetectionService.csproj"

# Copy source code and publish release build
COPY . .
RUN dotnet publish "FraudDetectionService.csproj" -c Release -o /app/publish /p:UseAppHost=false

# Stage 2: Production runtime stage using ASP.NET Core 8.0 Runtime
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final

WORKDIR /app

# Copy compiled binaries from publish stage
COPY --from=build /app/publish .

# Expose port 5000
EXPOSE 5000

# Set ASP.NET Core URL environment variable to listen on port 5000
ENV ASPNETCORE_URLS=http://+:5000

# Container entrypoint execution
ENTRYPOINT ["dotnet", "FraudDetectionService.dll"]
