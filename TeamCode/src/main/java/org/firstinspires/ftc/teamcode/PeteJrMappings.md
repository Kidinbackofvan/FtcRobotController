        // TODO: Done (9/2/24) - make sure your config has **motors** with these names (or change them)
        //   the encoders should be plugged into the slot matching the named motor
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
        public final Encoder par0, par1, perp;
        ...
        par0 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "par0")));
        par1 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "par1")));
        perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "perp")));

        // IMU orientation
        // TODO: Done (9/2/24) - fill in these values based on
        //   see https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html?highlight=imu#physical-hub-mounting
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

        // TODO: Done (9/2/24) - make sure your config has motors with these names (or change them)
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
        private DcMotor leftFront, rightFront, leftBack, rightBack;
        ...
        leftFront = hardwareMap.get(DcMotorEx.class, "l front");
        leftBack = hardwareMap.get(DcMotorEx.class, "l back");
        rightBack = hardwareMap.get(DcMotorEx.class, "r back");
        rightFront = hardwareMap.get(DcMotorEx.class, "r front");