import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.DocumentMetadata;
import software.amazon.awssdk.services.textract.model.TextractException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import scala.collection.JavaConverters._
import smithy4s.aws.*
import ciris.*
import org.http4s.ember.client.EmberClientBuilder
import cats.effect.IO
import cats.syntax.all.*
import cats.effect.kernel.Resource
import com.amazonaws.textract.Textract
import cats.effect.unsafe.implicits.global

// @main
def textract =
    val sourceDoc = "C:\\temp\\Brokers.png";
    val region = Region.EU_WEST_1;
    val textractClient = TextractClient.builder()
            .region(region)
            .build();

    detectDocText(textractClient, sourceDoc);
    textractClient.close();


def detectDocText(textractClient: TextractClient, sourceDoc: String)  = {
    val sourceStream = new FileInputStream(new File(sourceDoc));
    val sourceBytes = SdkBytes.fromInputStream(sourceStream);

    // Get the input Document object as bytes.
    val myDoc = Document.builder()
            .bytes(sourceBytes)
            .build();

    val detectDocumentTextRequest = DetectDocumentTextRequest.builder()
            .document(myDoc)
            .build();

    // Invoke the Detect operation.
    val textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
    val docInfo = textResponse.blocks().asScala;
    for ( block <- docInfo) {
        println("The block type is " + block.blockType().toString());
        println(block)
    }

    val documentMetadata = textResponse.documentMetadata();
    println("The number of pages in the document is " + documentMetadata.pages());

  }


@main
def run =
  resource.use { case (textract) =>
    val bytes = os.read.bytes(os.Path("C:\\temp\\Brokers.png"))
    val blobby = com.amazonaws.textract.ImageBlob( smithy4s.Blob(bytes))
    val doc = com.amazonaws.textract.Document(Some(blobby))
    val req = textract
      .detectDocumentText(doc)

    req.flatMap{
      IO.println
    }
}.unsafeRunSync()

def apiConfig: IO[AwsCredentials.Default] =
  (
    env("AWS_ACCESS_KEY_ID").as[String].secret,
    env("AWS_SECRET_ACCESS_KEY").as[String].secret,
  ).parMapN( (x,y) =>
    println(x.value)
    println(x.value)
    AwsCredentials.Default(x.value, y.value, None)
  ).load[IO]


val resource  =
  val client = EmberClientBuilder.default[IO].build
  val login = apiConfig
  for {
    httpClient <- EmberClientBuilder.default[IO].build
    awsEnv2 <- AwsEnvironment.default(httpClient, AwsRegion.EU_WEST_1)
    awsEnv <- Resource.eval( IO( AwsEnvironment.make[IO](httpClient, IO(AwsRegion.EU_WEST_1), login , IO(Timestamp.nowUTC()))))
    textract <- AwsClient(Textract, awsEnv2)
  } yield textract

