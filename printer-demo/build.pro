import static com.github.forax.pro.Pro.*;
import static com.github.forax.pro.builder.Builders.*;

packager.
    modules(
        "com.acme.printer.main@1.0/com.acme.printer.main.Main",
        "com.acme.printer.api@1.0",
        "com.acme.printer.factory@1.0"
    )

run(compiler, packager, runner)

/exit
