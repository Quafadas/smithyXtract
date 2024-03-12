import smithy4s.Blob
import smithy4s.schema.Schema
import com.amazonaws.textract.Block
import com.amazonaws.textract.BlockList
import smithy4s.json.Json
import com.github.plokhotnyuk.jsoniter_scala.core.JsonCodec
import alloy.SimpleRestJson
import com.amazonaws.textract
import smithy4s.codecs.PayloadError
import smithy4s.codecs.Decoder
import com.amazonaws.textract.BlockType
import java.nio.charset.StandardCharsets

@main
def run =
    val blockListDecoder = Json.payloadCodecs
        .withJsoniterCodecCompiler(
          Json.jsoniter.withMaxArity(1000000)
        )
        .decoders
        .fromSchema(BlockList.schema)

    val blockDecoder = Json.payloadCodecs
        .decoders
        .fromSchema(Block.schema)

    val blockEncoder = Json.payloadCodecs
        .encoders
        .fromSchema(Block.schema)


    val singleBlockSource = os.pwd / "testSimpleBlock.json"
    println(os.read(singleBlockSource))

    val b = blockDecoder.decode(Blob("""{"BlockType":"PAGE"}"""))
    println(b)

    val enc = blockEncoder.encode(Block(Some(BlockType.PAGE)))
    println(new String(enc.toArray, StandardCharsets.UTF_8))

    // val str = os.read(sourceDoc)
    // println(str.take(100))
    // val blocks: Either[PayloadError, Block] = b.decode(Blob(os.read.bytes(sourceDoc)))
    // println(blocks)
    // println(blocks.map(_.value.take(5)))
end run
