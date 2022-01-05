/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * Copyright (c) 2022 STMicroelectronics.
  * All rights reserved.
  *
  * This software is licensed under terms that can be found in the LICENSE file
  * in the root directory of this software component.
  * If no LICENSE file comes with this software, it is provided AS-IS.
  *
  ******************************************************************************
  */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "usb_device.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
I2C_HandleTypeDef hi2c1;

SPI_HandleTypeDef hspi1;

TIM_HandleTypeDef htim3;

/* USER CODE BEGIN PV */

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_I2C1_Init(void);
static void MX_SPI1_Init(void);
static void MX_TIM3_Init(void);
/* USER CODE BEGIN PFP */

uint8_t i2c1_pisiRegister(uint8_t, uint8_t, uint8_t);
void i2c1_beriRegistre(uint8_t, uint8_t, uint8_t*, uint8_t);
void initLSM303DLHC(void);

/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

uint8_t i2c1_pisiRegister(uint8_t naprava, uint8_t reg, uint8_t podatek)
{
  naprava <<= 1;
  return HAL_I2C_Mem_Write(&hi2c1, naprava, reg, I2C_MEMADD_SIZE_8BIT, &podatek, 1, 10);
}

void i2c1_beriRegistre(uint8_t naprava, uint8_t reg, uint8_t* podatek, uint8_t dolzina)
{
  if ((dolzina>1)&&(naprava==0x19))  // ce je naprava 0x19 moramo postaviti ta bit, ce zelimo brati vec zlogov
    reg |= 0x80;
  naprava <<= 1;
  HAL_I2C_Mem_Read(&hi2c1, naprava, reg, I2C_MEMADD_SIZE_8BIT, podatek, dolzina, dolzina);
}

void initLSM303DLHC()
{
  HAL_Delay(10);

  // Za potrebe testa, moramo testni napravi sporociti kateri senzor imamo
  #define OLD_SENSOR 0x73 // Odkomentiramo za LSM303DLHC / stari senzor
  //#define NEW_SENSOR 0x6E // Odkomentiramo za LSM303AGR / novi senzor

  #if defined(OLD_SENSOR) && !defined(NEW_SENSOR)
  i2c1_pisiRegister(0x1e, 0x4F, OLD_SENSOR); // Povemo testni napravi, da imamo stari senzor
  #elif !defined(OLD_SENSOR) && defined(NEW_SENSOR)
  i2c1_pisiRegister(0x1e, 0x4F, NEW_SENSOR); // Povemo testni napravi, da imamo novi senzor
  #else
  for(;;); // V primeru napake, pocakamo tukaj
  #endif
  HAL_Delay(100);

  // inicializiraj pospeskometer
  i2c1_pisiRegister(0x19, 0x20, 0x27);  // zbudi pospeskometer in omogoci osi
  i2c1_pisiRegister(0x19, 0x23, 0x88);  // nastavi posodobitev samo ko se prebere vrednost ter visoko locljivost
  i2c1_pisiRegister(0x19, 0x20, 0x47);	//odzivnost
  i2c1_pisiRegister(0x19, 0x23, 0x10);	//obcutljivost
}

/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */

  /* USER CODE END 1 */

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_I2C1_Init();
  MX_SPI1_Init();
  MX_TIM3_Init();
  MX_USB_DEVICE_Init();
  /* USER CODE BEGIN 2 */

  float getAxis(float X, float Y, float Z) {
      X = abs(X);
      Y = abs(Y);
      Z = abs(Z);
      if (X > Y) {
          if (X > Z){
        	  return 0;
          }
      }
      else if (Y > Z){
    	  return 1;
      }
      else{
    	  return 2;
      }

      return -1;
  }

  // zazenemo casovnik ter cakamo do preliva vrednosti
  HAL_TIM_Base_Start(&htim3);

  // pocistimo zastavico za preliv vrednosti
  __HAL_TIM_CLEAR_FLAG(&htim3, TIM_FLAG_UPDATE);

  __HAL_I2C_ENABLE(&hi2c1);

  initLSM303DLHC(); //zazeni init posp.

  int16_t meritev[6];
  meritev[0] = 0xaaab;// glava za zaznamek zacetek paketa

  int8_t cordXLow;
  int8_t cordXHigh;
  float cordX;

  int8_t cordYLow;
  int8_t cordYHigh;
  float cordY;

  int8_t cordZLow;
  int8_t cordZHigh;
  float cordZ;

  int bumpsX = 0;
  int bumpsY = 0;
  int bumpsZ = 0;
  int bumps = 0;
  int counter = 0;
  int paket = -1;
  float currentAccel = 0.0;
  float lastAccel = 0.0;
  int secondCounter = 0;
  int axis = 0;
  int once = 0;
  int once2 = 0;
  int once3 = 0;

  uint8_t buff[256];

  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {
	  HAL_Delay(10);
	  if(HAL_GPIO_ReadPin(GPIOA, GPIO_PIN_0)){
		  counter++;
		 if(counter > 4){
			 counter = 0;
			 once = 0;
			 once2 = 0;
			 once3 = 0;
			 bumps = 0;
			 bumpsX = 0;
			 bumpsY = 0;
			 bumpsZ = 0;
			 paket = -1;
			    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_15, GPIO_PIN_RESET);
			    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_14, GPIO_PIN_RESET);
			    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_13, GPIO_PIN_RESET);
			    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_12, GPIO_PIN_RESET);
			    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_11, GPIO_PIN_RESET);
				HAL_GPIO_WritePin(GPIOE, GPIO_PIN_8, GPIO_PIN_RESET);
		 }
	  }

		/*
		//do preliva vrednosti
		while (!__HAL_TIM_GET_FLAG(&htim3, TIM_FLAG_UPDATE));
		//pocistimo zastavico
		__HAL_TIM_CLEAR_FLAG(&htim3, TIM_FLAG_UPDATE);
		//stanje LED lucke
		HAL_GPIO_TogglePin(GPIOE, GPIO_PIN_10);
		*/

		if(counter == 1){	//ZBIRANJE PODATKOV

		    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_12, GPIO_PIN_SET);

			//PAKET
			paket = paket + 1;
			meritev[1] = paket;

			//X-os
			i2c1_beriRegistre(0x19, 0x29, (uint8_t *)&cordXHigh, 1);
			i2c1_beriRegistre(0x19, 0x28, (uint8_t *)&cordXLow, 1);
			meritev[1] = (cordXHigh << 8) | (cordXLow & 0xff);
			cordX = (meritev[1] >> 3) / 1000.0f;

			//Y-os
			i2c1_beriRegistre(0x19, 0x2B, (uint8_t *)&cordYHigh, 1);
			i2c1_beriRegistre(0x19, 0x2A, (uint8_t *)&cordYLow, 1);
			meritev[2] = (cordYHigh << 8) | (cordYLow & 0xff);
			cordY = (meritev[2] >> 3) / 1000.0f;

			//Z-os
			i2c1_beriRegistre(0x19, 0x2D, (uint8_t *)&cordZHigh, 1);
			i2c1_beriRegistre(0x19, 0x2C, (uint8_t *)&cordZLow, 1);
			meritev[3] = (cordZHigh << 8) | (cordZLow & 0xff);
			cordZ = (meritev[3] >> 3) / 1000.0f;

			lastAccel = currentAccel;
			currentAccel = sqrt(pow(cordX, 2) + pow(cordY, 2) + pow(cordZ, 2));
			axis = getAxis(cordX, cordY, cordZ);

			if (axis == 0) {
				if ((lastAccel - currentAccel) > 0.50) {
					bumpsX++;
				}
			}
			else if (axis == 1) {
				if ((lastAccel - currentAccel) > 0.50) {
					bumpsY++;
				}
			}
			else if (axis == 2) {
				if ((lastAccel - currentAccel) > 0.50) {
					bumpsZ++;
				}
			}

			int len = sprintf(buff, "{Pospeskometer[%i]-> \"X\":[%.3f],  \"Y\":[%.3f], \"Z\":[%.3f]}\n\r", paket, cordX, cordY, cordZ);
			CDC_Transmit_FS((uint8_t*)&buff, len);

		}

		else if(counter == 2){
			if(once2 < 1){
			    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_13, GPIO_PIN_SET);
				int len = sprintf(buff, "{\"Bumps X\":[%i],  \"Bumps Y\":[%i], \"Bumps Z\":[%i]}\n\r}", bumpsX, bumpsY, bumpsZ);
				CDC_Transmit_FS((uint8_t*)&buff, len);
				once2++;
			}
		}

		else if(counter == 3){
		    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_14, GPIO_PIN_SET);

			if(once < 1){
				int len = sprintf(buff, "{STOP}\n\r");
				CDC_Transmit_FS((uint8_t*)&buff, len);
				once++;
			}

		}

		else if(counter == 4){
			HAL_GPIO_WritePin(GPIOE, GPIO_PIN_15, GPIO_PIN_SET);
			//if(once3 < 1){
				bumps = bumpsX + bumpsY + bumpsZ;
				for(int i = 0; i < 256; i++){
					buff[i] = 0;
				}
				int len = sprintf(buff, "%i\n\r",bumps);
				CDC_Transmit_FS((uint8_t*)&buff, len);
				once3++;
			//}

		}

		while(HAL_GPIO_ReadPin(GPIOA, GPIO_PIN_0)){};


    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};
  RCC_PeriphCLKInitTypeDef PeriphClkInit = {0};

  /** Initializes the RCC Oscillators according to the specified parameters
  * in the RCC_OscInitTypeDef structure.
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI|RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_BYPASS;
  RCC_OscInitStruct.HSEPredivValue = RCC_HSE_PREDIV_DIV1;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = RCC_HSICALIBRATION_DEFAULT;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLMUL = RCC_PLL_MUL9;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB buses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV2;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_2) != HAL_OK)
  {
    Error_Handler();
  }
  PeriphClkInit.PeriphClockSelection = RCC_PERIPHCLK_USB|RCC_PERIPHCLK_I2C1;
  PeriphClkInit.I2c1ClockSelection = RCC_I2C1CLKSOURCE_HSI;
  PeriphClkInit.USBClockSelection = RCC_USBCLKSOURCE_PLL_DIV1_5;
  if (HAL_RCCEx_PeriphCLKConfig(&PeriphClkInit) != HAL_OK)
  {
    Error_Handler();
  }
}

/**
  * @brief I2C1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_I2C1_Init(void)
{

  /* USER CODE BEGIN I2C1_Init 0 */

  /* USER CODE END I2C1_Init 0 */

  /* USER CODE BEGIN I2C1_Init 1 */

  /* USER CODE END I2C1_Init 1 */
  hi2c1.Instance = I2C1;
  hi2c1.Init.Timing = 0x0000020B;
  hi2c1.Init.OwnAddress1 = 0;
  hi2c1.Init.AddressingMode = I2C_ADDRESSINGMODE_7BIT;
  hi2c1.Init.DualAddressMode = I2C_DUALADDRESS_DISABLE;
  hi2c1.Init.OwnAddress2 = 0;
  hi2c1.Init.OwnAddress2Masks = I2C_OA2_NOMASK;
  hi2c1.Init.GeneralCallMode = I2C_GENERALCALL_DISABLE;
  hi2c1.Init.NoStretchMode = I2C_NOSTRETCH_DISABLE;
  if (HAL_I2C_Init(&hi2c1) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure Analogue filter
  */
  if (HAL_I2CEx_ConfigAnalogFilter(&hi2c1, I2C_ANALOGFILTER_ENABLE) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure Digital filter
  */
  if (HAL_I2CEx_ConfigDigitalFilter(&hi2c1, 0) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN I2C1_Init 2 */

  /* USER CODE END I2C1_Init 2 */

}

/**
  * @brief SPI1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_SPI1_Init(void)
{

  /* USER CODE BEGIN SPI1_Init 0 */

  /* USER CODE END SPI1_Init 0 */

  /* USER CODE BEGIN SPI1_Init 1 */

  /* USER CODE END SPI1_Init 1 */
  /* SPI1 parameter configuration*/
  hspi1.Instance = SPI1;
  hspi1.Init.Mode = SPI_MODE_MASTER;
  hspi1.Init.Direction = SPI_DIRECTION_2LINES;
  hspi1.Init.DataSize = SPI_DATASIZE_4BIT;
  hspi1.Init.CLKPolarity = SPI_POLARITY_LOW;
  hspi1.Init.CLKPhase = SPI_PHASE_1EDGE;
  hspi1.Init.NSS = SPI_NSS_SOFT;
  hspi1.Init.BaudRatePrescaler = SPI_BAUDRATEPRESCALER_4;
  hspi1.Init.FirstBit = SPI_FIRSTBIT_MSB;
  hspi1.Init.TIMode = SPI_TIMODE_DISABLE;
  hspi1.Init.CRCCalculation = SPI_CRCCALCULATION_DISABLE;
  hspi1.Init.CRCPolynomial = 7;
  hspi1.Init.CRCLength = SPI_CRC_LENGTH_DATASIZE;
  hspi1.Init.NSSPMode = SPI_NSS_PULSE_ENABLE;
  if (HAL_SPI_Init(&hspi1) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN SPI1_Init 2 */

  /* USER CODE END SPI1_Init 2 */

}

/**
  * @brief TIM3 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM3_Init(void)
{

  /* USER CODE BEGIN TIM3_Init 0 */

  /* USER CODE END TIM3_Init 0 */

  TIM_ClockConfigTypeDef sClockSourceConfig = {0};
  TIM_MasterConfigTypeDef sMasterConfig = {0};

  /* USER CODE BEGIN TIM3_Init 1 */

  /* USER CODE END TIM3_Init 1 */
  htim3.Instance = TIM3;
  htim3.Init.Prescaler = 59999;
  htim3.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim3.Init.Period = 65535;
  htim3.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim3.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  if (HAL_TIM_Base_Init(&htim3) != HAL_OK)
  {
    Error_Handler();
  }
  sClockSourceConfig.ClockSource = TIM_CLOCKSOURCE_INTERNAL;
  if (HAL_TIM_ConfigClockSource(&htim3, &sClockSourceConfig) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim3, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM3_Init 2 */

  /* USER CODE END TIM3_Init 2 */

}

/**
  * @brief GPIO Initialization Function
  * @param None
  * @retval None
  */
static void MX_GPIO_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStruct = {0};

  /* GPIO Ports Clock Enable */
  __HAL_RCC_GPIOE_CLK_ENABLE();
  __HAL_RCC_GPIOC_CLK_ENABLE();
  __HAL_RCC_GPIOF_CLK_ENABLE();
  __HAL_RCC_GPIOA_CLK_ENABLE();
  __HAL_RCC_GPIOB_CLK_ENABLE();

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(GPIOE, CS_I2C_SPI_Pin|LD4_Pin|LD3_Pin|LD5_Pin
                          |LD7_Pin|LD9_Pin|LD10_Pin|LD8_Pin
                          |LD6_Pin, GPIO_PIN_RESET);

  /*Configure GPIO pins : DRDY_Pin MEMS_INT3_Pin MEMS_INT4_Pin MEMS_INT1_Pin
                           MEMS_INT2_Pin */
  GPIO_InitStruct.Pin = DRDY_Pin|MEMS_INT3_Pin|MEMS_INT4_Pin|MEMS_INT1_Pin
                          |MEMS_INT2_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_EVT_RISING;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(GPIOE, &GPIO_InitStruct);

  /*Configure GPIO pins : CS_I2C_SPI_Pin LD4_Pin LD3_Pin LD5_Pin
                           LD7_Pin LD9_Pin LD10_Pin LD8_Pin
                           LD6_Pin */
  GPIO_InitStruct.Pin = CS_I2C_SPI_Pin|LD4_Pin|LD3_Pin|LD5_Pin
                          |LD7_Pin|LD9_Pin|LD10_Pin|LD8_Pin
                          |LD6_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(GPIOE, &GPIO_InitStruct);

  /*Configure GPIO pin : B1_Pin */
  GPIO_InitStruct.Pin = B1_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(B1_GPIO_Port, &GPIO_InitStruct);

}

/* USER CODE BEGIN 4 */

/* USER CODE END 4 */

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* User can add his own implementation to report the HAL error return state */
  __disable_irq();
  while (1)
  {
  }
  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

