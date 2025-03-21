package Scripts;

import Classes.InstancePool;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.World.Box2D;
import JGamePackage.JGame.Classes.World.Image2D;


public class ParticleController extends WritableScript {

    Image2D ship;
    Box2D exhaustParticle;

    InstancePool<Box2D> particlePool;
    
    @Override
    public void Start() {
        ship = game.WorldNode.<Image2D>GetChild("Ship");
        exhaustParticle = game.WorldNode.<Box2D>GetChild("ParticleTemplate");

        exhaustParticle.SetParent(null);

        particlePool = new InstancePool<>(exhaustParticle, 30, false, 0);
    }

    @Override
    public void Tick(double dt) {
        if (game.TimeService.GetElapsedTicks() % 10 != 0) return;

        Box2D particle = particlePool.GetInstance();
        particle.Position = ship.Position;
        particle.SetParent(game.WorldNode.GetChild("ParticleNode"));

        game.TimeService.DelaySeconds(3, () -> {
            particlePool.ReturnInstance(particle);
        });
    }

    
}
