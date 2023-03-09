package microservicio.modayestilo.product;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import microservicio.modayestilo.product.entity.ProductEntity;
import org.jboss.logging.Logger;

import java.util.List;
@Slf4j
@Path("/api/modayestilo/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {
    private static final Logger LOGGER = Logger.getLogger(ProductoResource.class.getName());

    @GET
    public Uni<List<PanacheEntityBase>> getProduct() {
        return  ProductEntity.listAll();
    }
    @GET
    @Path("/{id}")
    public Uni<PanacheEntityBase> getByProductId(@PathParam("id") Long id) {
        return  ProductEntity.findById(id);
    }
    @POST
    public Uni<Response> addProduct(ProductEntity c){
        return Panache.withTransaction(c::persist)
                .replaceWith(Response.ok(c).status(Response.Status.CREATED)::build);
    }
    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id){
        return  Panache.withTransaction(() -> ProductEntity.deleteById(id))
                .map(deleted -> deleted
                        ?Response.ok().status(Response.Status.NO_CONTENT).build()
                        : Response.ok().status(Response.Status.NOT_FOUND).build());
    }
    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam("id") Long id, ProductEntity c){

        if(c == null || c.getDescripcionCorta() == null){
            throw new WebApplicationException("Product was not set on request.", HttpResponseStatus.UNPROCESSABLE_ENTITY.code());
        }
        return Panache
                .withTransaction(()-> ProductEntity.<ProductEntity> findById(id)
                        .onItem().ifNotNull().invoke(entity ->{
                            entity.setDescripcionCorta(c.getDescripcionCorta());
                            entity.setDescripcionLarga(c.getDescripcionLarga());
                            entity.setMarca(c.getMarca());
                            entity.setTalla(c.getTalla());
                            entity.setPrecio(c.getPrecio());
                            entity.setStock(c.getStock());
                            entity.setIdCategoria(c.getIdCategoria());
                            entity.setEnabledProduct(c.isEnabledProduct());
                        })
                )
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Response.Status.NOT_FOUND).build());
    }
}