using FraudDetectionService.Data;
using FraudDetectionService.DTOs;
using FraudDetectionService.Entities;
using FraudDetectionService.Enums;
using FraudDetectionService.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace FraudDetectionService.Controllers
{

    [ApiController]
    [Route("api/[controller]")]
    public class FraudController : ControllerBase
    {

        private readonly IFraudService _fraudService;


        public FraudController(IFraudService fraudService)
        {
            _fraudService = fraudService;
        }



        [HttpPost("check")]
        public async Task<IActionResult> CheckFraud(FraudCheckRequest request)
        {

            var response = await _fraudService.CheckFraudAsync(request);


            return Ok(response);

        }

    }
}
