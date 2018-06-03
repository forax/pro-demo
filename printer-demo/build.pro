import static com.github.forax.pro.Pro.*;
import static com.github.forax.pro.builder.Builders.*;

packager.
    moduleMetadata(list(
        "printer.main@1.0/com.acme.printer.main.Main",
        "printer.api@1.0",
        "printer.factory@1.0"
    ))

run(compiler, packager, runner)

/exit
